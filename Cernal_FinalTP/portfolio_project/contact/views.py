from django.shortcuts import render, redirect
from django.http import JsonResponse
from .forms import ContactUserForm, MessageForm
from .models import Message
from django.contrib import messages
from django.contrib.auth.forms import UserCreationForm
from django.contrib.auth.decorators import login_required, user_passes_test
from django.views.decorators.csrf import csrf_exempt


@login_required
@csrf_exempt
def send_message(request):
    if request.method == 'POST':
        content = request.POST.get('content', '').strip()
        if content:
            Message.objects.create(user=request.user, content=content)
            return JsonResponse({'status': 'success', 'response': 'Message sent successfully!'})
        else:
            return JsonResponse({'status': 'error', 'response': 'Empty message.'})
    return JsonResponse({'status': 'error', 'response': 'Invalid request.'})


@login_required
def contact_view(request):
    if request.method == 'POST':
        form = MessageForm(request.POST)
        if form.is_valid():
            message_obj = form.save(commit=False)
            message_obj.user = request.user
            message_obj.save()
            messages.success(request, 'Your message has been sent to the admin.')
            return redirect('contact')
        else:
            messages.error(request, 'Invalid form submission.')
    else:
        form = MessageForm()

    return render(request, 'contact/contact.html', {'form': form})


@user_passes_test(lambda u: u.is_superuser)
def admin_inbox(request):
    messages_list = Message.objects.all().order_by('-timestamp')
    return render(request, 'contact/admin_inbox.html', {'messages': messages_list})


def register_view(request):
    if request.method == 'POST':
        form = UserCreationForm(request.POST)
        if form.is_valid():
            form.save()
            messages.success(request, 'Account created successfully. You can now log in.')
            return redirect('login')
    else:
        form = UserCreationForm()
    return render(request, 'registration/register.html', {'form': form})
