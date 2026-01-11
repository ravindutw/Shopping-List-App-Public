import boto3
from django.conf import settings


class DynamoDBHandler:
    def __init__(self, table_name=None):
        self.table_name = table_name or settings.DYNAMODB_TABLE_NAME
        self.dynamodb = boto3.client('dynamodb', region_name=settings.AWS_REGION)

    def put_item(self, item):
        """
        Put an item into DynamoDB table
        item: dictionary with attribute names as keys and values as boto3 attribute values
        """
        self.dynamodb.put_item(
            TableName=self.table_name,
            Item=item
        )

    def get_item(self, key):
        """
        Get an item from DynamoDB table
        key: dictionary with key attribute names and values
        """
        response = self.dynamodb.get_item(
            TableName=self.table_name,
            Key=key
        )
        return response.get('Item')

    def update_item(self, key, update_expression, expression_attribute_values, expression_attribute_names=None):
        """
        Update an item in DynamoDB table
        """
        params = {
            'TableName': self.table_name,
            'Key': key,
            'UpdateExpression': update_expression,
            'ExpressionAttributeValues': expression_attribute_values
        }
        if expression_attribute_names:
            params['ExpressionAttributeNames'] = expression_attribute_names
        
        self.dynamodb.update_item(**params)

    def scan(self):
        """
        Scan the DynamoDB table and return all items
        """
        response = self.dynamodb.scan(TableName=self.table_name)
        return response.get('Items', [])

    def query(self, key_condition_expression, expression_attribute_values):
        """
        Query the DynamoDB table
        """
        response = self.dynamodb.query(
            TableName=self.table_name,
            KeyConditionExpression=key_condition_expression,
            ExpressionAttributeValues=expression_attribute_values
        )
        return response.get('Items', [])

    def delete_item(self, key):
        """
        Delete an item from DynamoDB table
        """
        self.dynamodb.delete_item(
            TableName=self.table_name,
            Key=key
        )
