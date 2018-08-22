#!/usr/bin/python3


import logging


class sensor:

  def __init__(self):
    self.logger = logging.getLogger('sensing.sensor')

  def read(self):
    self.logger.info('This is from sensor')
