import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { PaymentBlockService } from '@core/services/om/payment-block.service';
import { SidebarService } from '@core/services';

@Component({
  selector: 'app-generate-file-report',
  templateUrl: './generate-file-report.component.html',
  styleUrls: ['./generate-file-report.component.scss'],
})
export class GenerateFileReportComponent implements OnInit {
  displayedColumns: string[] = [
    'docType',
    'compCode',
    'accDocNo',
    'fiscalYear',
    'valueOld',
    'valueNew',
    'reason',
    'userPost',
    'postDate',
    'username',
  ];

  dataSource = new MatTableDataSource([]);

  constructor(
    private paymentBlockService: PaymentBlockService,
    private sidebarService: SidebarService
  ) {}

  ngOnInit() {
    this.sidebarService.updatePageType('generate');
    this.sidebarService.updateNowPage('generateReport');
    // this.getLog();
  }

  // getLog() {
  //   this.paymentBlockService.getPaymentBlockLog().then((data) => {
  //     console.log(data);
  //     const response = data as any;
  //     if (response.status === 200) {
  //       this.dataSource = new MatTableDataSource(response.data);
  //     }
  //   });
  // }
}
