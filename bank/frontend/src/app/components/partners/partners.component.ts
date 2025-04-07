import { Component } from '@angular/core';
import { PartnerService } from '../../services/partner.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PartnerRequestDTO } from '../../models/PartnerRequestDTO';

@Component({
  selector: 'app-partners',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './partners.component.html',
  styleUrl: './partners.component.scss'
})
export class PartnersComponent {
  constructor(private partnerService: PartnerService) {}

  partners: PartnerRequestDTO[] = [];
  newPartner: PartnerRequestDTO = {
    id: '',
    alias: '',
    type: '',
    direction: 'INBOUND',
    application: '',
    processedFlowType: 'MESSAGE',
    description: ''
  };


  ngOnInit() {
    this.partnerService.getAllPartners().subscribe(data => this.partners = data);
  }

  //TODO: with reactive forms for improvment
  addPartner() {
    this.partnerService.createPartner(this.newPartner).subscribe(() => {
      this.ngOnInit();
      this.newPartner = {
        id:'',
        alias: '',
        type: '',
        direction: 'INBOUND',
        application: '',
        processedFlowType: 'MESSAGE',
        description: ''
      };
    });
  }

  deletePartner(id: string) {
    this.partnerService.deletePartner(id).subscribe(() => this.ngOnInit());
  }
}
