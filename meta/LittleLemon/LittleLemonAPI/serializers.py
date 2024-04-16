from rest_framework import serializers
from .models import *
from django.contrib.auth.models import User


class CategorySerializer(serializers.ModelSerializer):
    class Meta:
        model = Category
        fields = ['title']
    

class MenuItemSerializer(serializers.ModelSerializer):
    class Meta:
        model = MenuItem
        fields = ['id','title','price','featured','category']
        
        
class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ['id', 'username', 'email']
      
class CartSerializer(serializers.ModelSerializer):
    class Meta:
        model = Cart
        fields = ['user', 'menuitem', 'quantity', 'unit_price', 'price']
    
class OrderItemSerializer(serializers.ModelSerializer):
    menuitem = MenuItemSerializer(read_only=True)
    # menuitem_id = serializers.IntegerField(write_only=True)
    class Meta:
        model = OrderItem
        fields = ['order', 'menuitem', 'menuitem_id', 'quantity', 'unit_price', 'price']
        
class OrderSerializer(serializers.ModelSerializer):
    # id = serializers.IntegerField()
    class Meta:
        model = Order
        fields = ['id','user','total','status','delivery_crew','date']
class OrderPutSerializer(serializers.ModelSerializer):
    class Meta:
        model = Order
        fields = ['delivery_crew']
    
        


    

 
        
        
    
    