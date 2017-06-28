#!/usr/bin/python3
# -*- coding: utf-8 -*-

from base.plugin.abstract_plugin import AbstractPlugin
from base.model.enum.content_type import ContentType
import json

class DiskLimit(AbstractPlugin):
    def __init__(self, task, context):
        super(DiskLimit, self).__init__()
        self.task = task
        self.context = context
        self.message_code = self.get_message_code()
        self.logger = self.get_logger()


    def save_mail(self, status):
        cols = ['command', 'mailstatus', 'timestamp'];
        values = ['disk_limit', status, self.timestamp()]
        self.db_service.update('mail', cols, values)

    def set_mail_content_withusage(self,mail_content,disk_limit,usage):
        if mail_content.__contains__('{limit}'):
            mail_content = str(mail_content).replace('{limit}', str(disk_limit));
        if mail_content.__contains__('{usage}'):
            mail_content = str(mail_content).replace('{usage}', str(usage));
        if mail_content.__contains__('{ahenk}'):
            mail_content = str(mail_content).replace('{ahenk}', str(self.Ahenk.dn()));

    def handle_task(self):
        disk_limit = self.task['DiskLimitPercentage'];
        usage= (self.Hardware.Disk.used()/self.Hardware.Disk.total())*100;

        self.logger.debug('[DiskLimit] usage:  ' + str(usage));
# usage limit control
        if self.context.is_mail_send():
            self.set_mail_content_withusage(self.context.get_mail_content(), disk_limit, usage)
            mail = self.db_service.select('mail', '*', 'command = \'disk_limit\' ')
            if len(mail) < 1:
                self.context.create_response(code=self.message_code.TASK_PROCESSED.value,
                                             message='Disk Kullanım Limiti başarıyla oluşturuldu.',
                                             data=json.dumps({
                                                 'Result': 'İşlem Başarı ile gercekleştirildi',
                                                 'mail_content': str(self.context.get_mail_content()),
                                                 'mail_subject': str(self.context.get_mail_subject()),
                                                 'mail_send': self.context.is_mail_send()
                                             }),
                                             content_type=ContentType.APPLICATION_JSON.value)
                self.save_mail(1)
            else:
                status = mail[0][2];
                if status == 0:
                    self.context.create_response(code=self.message_code.TASK_PROCESSED.value,
                                                 message='Disk Kullanım Limiti başarıyla oluşturuldu.',
                                                 data=json.dumps({
                                                     'Result': 'İşlem Başarı ile gercekleştirildi',
                                                     'mail_content': str(self.context.get_mail_content()),
                                                     'mail_subject': str(self.context.get_mail_subject()),
                                                     'mail_send': self.context.is_mail_send()
                                                 }),
                                                 content_type=ContentType.APPLICATION_JSON.value)
                    self.save_mail(1)

                else:
                    self.context.create_response(code=self.message_code.TASK_PROCESSED.value,
                                                 message='Disk Kullanım Limiti başarıyla oluşturuldu.',
                                                 data=json.dumps({
                                                     'Result': '',
                                                     'mail_send': False
                                                 }),
                                                 content_type=ContentType.APPLICATION_JSON.value)




def handle_task(task, context):
    diskLimit = DiskLimit(task, context)
    diskLimit.handle_task()
