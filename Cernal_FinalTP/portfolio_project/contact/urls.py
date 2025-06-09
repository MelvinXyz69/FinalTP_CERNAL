# contact/urls.py

from django.urls import path
from . import views

urlpatterns = [
    path('', views.contact_view, name='contact'),
    path('admin_inbox/', views.admin_inbox, name='admin_inbox'),  # <-- changed from 'inbox/'
    path('send-message/', views.send_message, name='send_message'),
]
