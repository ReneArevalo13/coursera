from django.urls import path
from . import views

urlpatterns = [
    path('menu-items/', views.MenuItemView.as_view()),
    path('menu-items/<int:pk>', views.SingleMenuItemView.as_view()),
    path('menu-items/category', views.CategoryView.as_view()),
    path('groups/managers/users', views.ManagerView.as_view()),
    path('groups/managers/users/<int:pk>', views.ManagerSingleView.as_view()),
    path('groups/delivery-crew/users', views.DeliveryCrewView.as_view()),
    path('groups/delivery-crew/users/<int:pk>', views.DeliveryCrewSingleView.as_view()),
    path('cart/menu-items/', views.CartView.as_view()),
    path('orders/', views.OrdersView.as_view()),
    

]