import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MessageService } from '../../services/message.service';
import { Message } from '../../models/messages';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-messages',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './messages.component.html',
  styleUrl: './messages.component.scss'
})
export class MessagesComponent {
  messages: Message[] = [];
  selectedMessage: Message | null = null;
  noMessage: boolean = false;
  newMessage: string = '';

  constructor(private messageService: MessageService) { }

  ngOnInit() {
    this.loadMessages();
  }

  loadMessages() {
    this.messageService.getAllMessages().subscribe(data => {
      if (Array.isArray(data)) {
        this.messages = data;
        this.noMessage = false;
      } else {
        this.noMessage = true;
      }
    });
  }

  sendMessage() {
    if (this.newMessage.trim()) {
      this.messageService.sendMessage(this.newMessage).subscribe(() => {
        this.newMessage = '';
        this.loadMessages();
      });
    }
  }

  showDetails(msg: Message) {
    this.selectedMessage = msg;
  }

}
