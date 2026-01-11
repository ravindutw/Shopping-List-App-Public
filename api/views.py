from django.shortcuts import render, redirect
from django.contrib.auth import authenticate, login as auth_login, logout as auth_logout
from django.contrib.auth.decorators import login_required
from django.views.decorators.http import require_http_methods
from django.http import JsonResponse
from django.middleware.csrf import get_token
from django.views.decorators.csrf import ensure_csrf_cookie
from django.contrib.auth.hashers import make_password
import json

from .models import User
from .services import ItemService, DisplayService
from .utils import next_id


# Template views
@login_required
def index(request):
    """Render the main shopping list page"""
    return render(request, 'index.html')


@ensure_csrf_cookie
def login_view(request):
    """Render the login page"""
    if request.user.is_authenticated:
        return redirect('/')
    return render(request, 'login.html')


@login_required
def create_user_view(request):
    """Render the create user page (admin only)"""
    if request.user.role != 'ADMIN':
        return redirect('/')
    return render(request, 'create-user.html')


# API views
@require_http_methods(["GET"])
def csrf_token(request):
    """Return CSRF token"""
    token = get_token(request)
    return JsonResponse({
        'token': token,
        'headerName': 'X-CSRFToken'
    })


@login_required
@require_http_methods(["POST"])
def add_grocery(request):
    """Add a new grocery item"""
    try:
        data = json.loads(request.body)
        name = data.get('name', '').strip()
        
        if not name:
            return JsonResponse(
                {'error': 'Name cannot be empty or blank'},
                status=400
            )
        
        item_service = ItemService()
        item_service.new_item(request.user, name, 'HKU')
        
        return JsonResponse(
            {'message': 'Item added successfully'},
            status=200
        )
    except Exception as e:
        print(f"Error adding item: {str(e)}")
        return JsonResponse(
            {'error': 'Error occurred while adding item'},
            status=500
        )


@login_required
@require_http_methods(["POST"])
def toggle_grocery(request):
    """Toggle the checked status of a grocery item"""
    try:
        data = json.loads(request.body)
        item_id = data.get('id', '').strip()
        
        if not item_id:
            return JsonResponse(
                {'error': 'ID cannot be empty or blank'},
                status=400
            )
        
        item_service = ItemService()
        item_service.check_item(item_id, request.user)
        
        return JsonResponse(
            {'message': 'Item toggled successfully'},
            status=200
        )
    except Exception as e:
        print(f"Error toggling item: {str(e)}")
        return JsonResponse(
            {'error': 'Error occurred while toggling item'},
            status=500
        )


@login_required
@require_http_methods(["GET"])
def fetch_groceries(request):
    """Fetch all unchecked grocery items"""
    try:
        display_service = DisplayService()
        items = display_service.get_items()
        return JsonResponse(items, safe=False, status=200)
    except Exception as e:
        print(f"Error fetching items: {str(e)}")
        return JsonResponse(
            {'error': 'Error occurred while fetching items'},
            status=500
        )


@login_required
@require_http_methods(["POST"])
def create_new_user(request):
    """Create a new user (admin only)"""
    if request.user.role != 'ADMIN':
        return JsonResponse(
            {'error': 'Unauthorized'},
            status=403
        )
    
    try:
        data = json.loads(request.body)
        username = data.get('userName', '').strip()
        name = data.get('name', '').strip()
        role = data.get('role', 'USER').strip()
        password = data.get('password', '').strip()
        
        if not username or not name or not password:
            return JsonResponse(
                {'error': 'All fields are required'},
                status=400
            )
        
        # Check if username already exists
        if User.objects.filter(username=username).exists():
            return JsonResponse(
                {'error': 'Username already exists'},
                status=400
            )
        
        # Get last user ID and generate next one
        last_user = User.objects.order_by('-user_id').first()
        if last_user:
            user_id = next_id(last_user.user_id)
        else:
            user_id = 'USER-001'
        
        # Create new user
        user = User(
            user_id=user_id,
            username=username,
            name=name,
            role=role
        )
        user.set_password(password)
        user.save()
        
        return JsonResponse(
            {'message': 'User created successfully'},
            status=200
        )
    except Exception as e:
        print(f"Error creating user: {str(e)}")
        return JsonResponse(
            {'error': 'Error occurred while creating user'},
            status=500
        )

