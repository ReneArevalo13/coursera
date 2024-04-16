
from rest_framework import status, generics
from rest_framework.decorators import api_view, permission_classes
from rest_framework.permissions import IsAuthenticated, IsAdminUser, AllowAny
from .models import *
from .serializers import *
from django.contrib.auth.models import User, Group
from django.shortcuts import get_object_or_404
from django.http import HttpResponse
from rest_framework.response import Response
from decimal import Decimal
from datetime import date



# Create your views here.

class MenuItemView(generics.ListAPIView,  generics.ListCreateAPIView):
    queryset = MenuItem.objects.all()
    serializer_class = MenuItemSerializer
    def get_permissions(self):
        if self.request.method == 'POST':
            return [IsAdminUser()]
        return [AllowAny()]

class SingleMenuItemView(generics.RetrieveUpdateDestroyAPIView):
    queryset = MenuItem.objects.all()
    serializer_class = MenuItemSerializer
    
    def get_permissions(self):
        if self.request.method == 'POST' or self.request.method == 'PUT' \
            or self.request.method == 'PATCH' or self.request.method == 'DELETE':
                return 
        return [AllowAny()]
    
class CategoryView(generics.ListAPIView, generics.ListCreateAPIView):
    queryset = Category.objects.all()
    serializer_class = CategorySerializer
    
class ManagerView(generics.ListAPIView):
    serializer_class = UserSerializer
    permission_classes = [IsAdminUser]
    
    def get_queryset(self):
        managers = Group.objects.get(name='Manager')
        queryset = User.objects.filter(groups=managers)
        return queryset
    def post(self, request, *args, **kwargs):
        username = request.data['username']
        if username:
            user = get_object_or_404(User, username=username)
            managers = Group.objects.get(name='Manager')
            managers.user_set.add(user)
            return HttpResponse(status=204)    
        
class ManagerSingleView(generics.RetrieveDestroyAPIView):
    serializer_class = UserSerializer
    permission_classes = [IsAdminUser]
    
    def get_queryset(self):
        managers = Group.objects.get(name='Manager')
        queryset = User.objects.filter(groups=managers)
        return queryset
        
class DeliveryCrewView(generics.ListAPIView):
    serializer_class = UserSerializer
    permission_classes = [IsAdminUser]
    
    def get_queryset(self):
        deliverycrew = Group.objects.get(name='Delivery crew')
        queryset = User.objects.filter(groups=deliverycrew)
        return queryset
    def post(self, request, *args, **kwargs):
        username = request.data['username']
        if username:
            user = get_object_or_404(User, username=username)
            deliverycrew = Group.objects.get(name='Delivery crew')
            deliverycrew.user_set.add(user)
            return HttpResponse(status=204) 

class DeliveryCrewSingleView(generics.RetrieveDestroyAPIView):
    serializer_class = UserSerializer
    permission_classes = [IsAdminUser]
    
    def get_queryset(self):
        deliverycrew = Group.objects.get(name='Delivery crew')
        queryset = User.objects.filter(groups=deliverycrew)
        return queryset
    
class CartView(generics.ListAPIView):
    serializer_class = CartSerializer
    permission_classes = [IsAuthenticated]
    
    def get_queryset(self):
        user = self.request.user
        return Cart.objects.filter(user=user)
        
    def post(self, request, *args, **kwargs):
        
        menuitem = request.data.get('menuitem')
        quantity = request.data.get('quantity')
        unit_price = MenuItem.objects.get(pk=menuitem).price
        quantity = int(quantity)
        price = unit_price * quantity
        
        data = {
            'menuitem': menuitem,
            'quantity': quantity,
            'unit_price': unit_price,
            'price': price,
            'user': request.user.id,
        }
        
        serializer = CartSerializer(data=data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)

        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
    
    def delete(self, request):
        user = request.user
        Cart.objects.filter(user=user).delete()
        return Response(status=204)
    
    
class OrdersView(generics.ListCreateAPIView):
    serializer_class = OrderSerializer
    permission_classes = [IsAuthenticated]
       
    def get_queryset(self):
        user = self.request.user
        if user.groups.filter(name='Manager').exists():
            return Order.objects.all()
        return Order.objects.filter(user=user)
    
    def post(self, request, *args, **kwargs):
        
        cart = Cart.objects.filter(user=request.user)
        total = self.cart_total(cart)
        
        order_data = {
            'user': request.user.id,
            'total': total,
            'date': date.today()
        }
        order = OrderSerializer(data=order_data)
        if order.is_valid(raise_exception=True):
            print('saving order')
            order.save()
                
        for item in cart:
            print('going through items')
            print(order.data)
            print(item.menuitem.id)
            
            orderitem_data = {
                'order': order.data['id'],
                'menuitem': item.menuitem,
                'menuitem_id': item.menuitem.id,
                'quantity': item.quantity,
                'unit_price': item.unit_price,
                'price': item.price
            }
            
            print('have order item data')
            print(orderitem_data)
            orderitem_serialized = OrderItemSerializer(data=orderitem_data)
            
            if orderitem_serialized.is_valid(raise_exception=True):
                # print(orderitem_serialized.data)
                print('saving order item')
                orderitem_serialized.save()
                print('item saved')
                
        cart.delete()
        return Response(order.data, status=status.HTTP_201_CREATED)
            
    
    
    def delete(self, request):
        user = self.request.user
        Order.objects.filter(user=user).delete()
        return Response(status=204)
    
    def cart_total(self, cart_items):
        total = Decimal(0)
        for item in cart_items:
            total += item.price
        return total
    
    # def get_permissions(self):    
        
    #     if self.request.method == 'GET' or 'POST' : 
    #         permission_classes = [IsAuthenticated]
    #     else:
    #         permission_classes = [IsAuthenticated | IsAdminUser]
    #     return[permission() for permission in permission_classes]
        

class SingleOrderView(generics.ListAPIView):
    serializer_class = OrderItemSerializer

    def get_queryset(self, *args, **kwargs):
        order_id = self.kwargs['pk']
        query = OrderItem.objects.filter(order_id=order_id)
        return query
    
    # changes delivery status 
    def patch(self, *args, **kwargs):
        order = Order.objects.get(pk=self.kwargs['pk'])
        order.status = not order.status
        order.save()
        return Response(status=200)
    
    # set delivery crew
    def put(self, request, *args, **kwargs):
        order = Order.objects.get(pk=self.kwargs['pk'])
        deliverycrew = request.data["delivery_crew"]
        delivery_data = {"delivery_crew": deliverycrew}
        serialized_item = OrderSerializer(order, data=delivery_data, partial=True)
        serialized_item.is_valid(raise_exception=True)
        serialized_item.save()
        return Response(status=200)