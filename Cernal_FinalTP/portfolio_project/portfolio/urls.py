from django.urls import path
from . import views

urlpatterns = [
    path('', views.home, name='home'),  # Example: maps root URL to home view
]