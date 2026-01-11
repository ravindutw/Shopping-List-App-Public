from .dynamodb_handler import DynamoDBHandler
from .utils import generate_random_id, get_date_and_time
from django.conf import settings


class ItemService:
    def __init__(self):
        self.db = DynamoDBHandler()

    def new_item(self, user, name, location='NULL'):
        """
        Create a new shopping list item
        """
        item_id = generate_random_id()
        date = get_date_and_time()

        item = {
            'id': {'S': item_id},
            'name': {'S': name},
            'created_date': {'S': date},
            'created_user': {'S': user.username},
            'location': {'S': location},
            'checked': {'S': 'false'}
        }

        self.db.put_item(item)

    def check_item(self, item_id, user):
        """
        Mark an item as checked
        """
        date = get_date_and_time()

        key = {'id': {'S': item_id}}
        update_expression = 'SET checked = :checked, checked_user = :user, checked_date = :date'
        expression_attribute_values = {
            ':checked': {'S': 'true'},
            ':user': {'S': user.username},
            ':date': {'S': date}
        }

        self.db.update_item(key, update_expression, expression_attribute_values)


class DisplayService:
    def __init__(self):
        self.db = DynamoDBHandler()

    def get_items(self):
        """
        Get all unchecked shopping list items
        """
        items = self.db.scan()
        result = []

        for item in items:
            if item.get('checked', {}).get('S') == 'false':
                result.append({
                    'id': item.get('id', {}).get('S', ''),
                    'name': item.get('name', {}).get('S', ''),
                    'checked': item.get('checked', {}).get('S', 'false')
                })

        return result
