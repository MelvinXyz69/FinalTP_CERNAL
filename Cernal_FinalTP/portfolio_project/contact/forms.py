from django import forms
from .models import ContactUser

class ContactUserForm(forms.ModelForm):
    class Meta:
        model = ContactUser
        fields = ['username']
        
from django import forms
from .models import Message

class MessageForm(forms.ModelForm):
    class Meta:
        model = Message
        fields = ['content']
        widgets = {
            'content': forms.Textarea(attrs={'rows': 4, 'placeholder': 'Enter your message here...'}),
        }
