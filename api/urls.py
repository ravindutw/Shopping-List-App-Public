from django.urls import path
from . import views

app_name = 'api'

urlpatterns = [
    # Template views
    path('', views.index, name='index'),
    path('login', views.login_view, name='login'),
    path('admin-portal/new-user', views.create_user_view, name='create-user'),
    
    # API endpoints
    path('csrf-token', views.csrf_token, name='csrf-token'),
    path('app-api/groceries/add', views.add_grocery, name='add-grocery'),
    path('app-api/groceries/toggle', views.toggle_grocery, name='toggle-grocery'),
    path('app-api/groceries/fetch', views.fetch_groceries, name='fetch-groceries'),
    path('admin-api/admin/new-user', views.create_new_user, name='create-new-user'),
]
