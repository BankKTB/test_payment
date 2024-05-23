import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { PaymentBlockService } from '@core/services/om/payment-block.service';
import { SidebarService } from '@core/services';
import { MatDialog } from '@angular/material/dialog';
import { DialogOmLogComponent } from '@shared/component/dialog-om-log/dialog-om-log.component';
import { PaymentRunErrorService } from '@core/services/payment-run-error/payment-run-error.service';
import { DialogPaymentRunErrorDetailComponent } from '@shared/component/dialog-payment-run-error-detail/dialog-payment-run-error-detail.component';

@Component({
  selector: 'app-payment-run-error',
  templateUrl: './payment-run-error.component.html',
  styleUrls: ['./payment-run-error.component.scss'],
})
export class PaymentRunErrorComponent implements OnInit {
  displayedColumns: string[] = ['compCode', 'accDocNo', 'fiscalYear'];

  dataSource = new MatTableDataSource([]);

  constructor(
    private paymentRunErrorService: PaymentRunErrorService,
    private sidebarService: SidebarService,
    private dialog: MatDialog
  ) {}

  ngOnInit() {
    this.sidebarService.updatePageType('om');
    this.sidebarService.updateNowPage('omLog');
    this.getLog();
  }

  getLog() {
    this.paymentRunErrorService.findAll().then((data) => {
      console.log('getPaymentBlockLog');
      console.log(data);
      const response = data as any;
      if (response.status === 200) {
        this.dataSource = new MatTableDataSource(response.data);
      }
    });
  }

  openDialogPaymentRunErrorDetail(document) {
    console.log(document);

    const dialogRef = this.dialog.open(DialogPaymentRunErrorDetailComponent, {
      width: '1200px',
      data: {
        document,
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
      }
    });
  }
}
