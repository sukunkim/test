#!/usr/bin/python3


import logging
import logging.handlers
import time


class jsonFileDataMgr:

  def __init__(self):
    self.logger = logging.getLogger('sensing.jsonFileDataMgr')

    formatter = logging.Formatter('%(message)s')
    handler = logging.handlers.TimedRotatingFileHandler(
      filename = 'json/' + time.strftime('%Y-%m-%d.json'), when = 'D')
    handler.setFormatter(formatter)

    self.writer = logging.getLogger('jsonFileDataMgr')
    self.writer.addHandler(handler)
    self.writer.setLevel(logging.DEBUG)

  def write(self):
    self.logger.info('This is from jsonFileDataMgr')

    self.writer.debug('a = 0, b = 1, c = 2, d = 3')
