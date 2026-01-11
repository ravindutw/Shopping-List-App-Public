import uuid
from datetime import datetime
import pytz


def get_date_and_time(format_str='%Y-%m-%d %H:%M:%S'):
    """
    Get current date and time in Colombo timezone
    """
    colombo_tz = pytz.timezone('Asia/Colombo')
    colombo_time = datetime.now(colombo_tz)
    return colombo_time.strftime(format_str)


def generate_random_id():
    """
    Generate a random UUID
    """
    return str(uuid.uuid4())


def next_id(current_id):
    """
    Generate the next sequential ID based on the current ID
    e.g., USER-001 -> USER-002
    """
    parts = current_id.split('-')
    prefix = parts[0]
    number = int(parts[1])
    number += 1
    
    # Maintain the same number of digits
    num_length = len(parts[1])
    new_number = str(number).zfill(num_length)
    
    return f"{prefix}-{new_number}"
