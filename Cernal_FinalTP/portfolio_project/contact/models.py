from django.db import models
from django.contrib.auth.models import User


class ContactUser(models.Model):
    username = models.CharField(max_length=50, unique=True)  # Make usernames unique

    def __str__(self):
        return self.username

class Message(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE)
    content = models.TextField()
    timestamp = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return f"{self.user.username}: {self.content[:30]}"