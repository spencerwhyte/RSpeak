# Hashem Shawqi and Spencer Whyte
# This module contains all helper miscelanneous helper
# functions that aren't worthy of having a dedicated module

import string
import random

def random_alphanumeric( length ):
	charpool = []
	random_string = ""

	for number in range( 10 ):
		charpool.append( str( number ) )
	for alphabet in string.ascii_lowercase:
		charpool.append( alphabet )
	for alphabet in string.ascii_uppercase:
		charpool.append( alphabet )

	for i in range( length ):
		random_string += random.choice( charpool )

	return random_string