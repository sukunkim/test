#!/usr/bin/python3


import logging
import logging.handlers
import time

from sensor import sensor
from jsonFileDataMgr import jsonFileDataMgr


class sensing:

  def __init__(self):
    self.logger = logging.getLogger('sensing.sensing')

    self.tmpSensor = sensor()
    self.tmpJsonFileDataMgr = jsonFileDataMgr()

  def run(self):
    self.logger.info('This is from sensing')

    self.tmpSensor.read()
    self.tmpJsonFileDataMgr.write()


if __name__ == "__main__":

  # Root
  formatter = logging.Formatter(
    '%(asctime)s - %(name)s - %(levelname)s - %(message)s')
  handler = logging.handlers.TimedRotatingFileHandler(
    filename = 'log/' + time.strftime('%Y-%m-%d.log'), when = 'D')
  handler.setFormatter(formatter)

  logger = logging.getLogger('sensing')
  logger.addHandler(handler)
  logger.setLevel(logging.WARN)

  # Logger hierarchy
  logging.getLogger('sensing.sensing').setLevel(logging.WARN)
  logging.getLogger('sensing.sensor').setLevel(logging.INFO)
  logging.getLogger('sensing.jsonFileDataMgr').setLevel(logging.DEBUG)


  tmpSensing = sensing()
  logger.info('This is from root')
  tmpSensing.run()
