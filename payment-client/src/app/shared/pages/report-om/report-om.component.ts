import { Component, OnInit } from '@angular/core';
import { PaymentReportService } from '@core/services/payment-report/payment-report.service';
import { ActivatedRoute } from '@angular/router';
import { SidebarService } from '@core/services';

@Component({
  selector: 'app-report-om',
  templateUrl: './report-om.component.html',
  styleUrls: ['./report-om.component.scss'],
})
export class ReportOmComponent implements OnInit {
  report = {
    totalAmount: '20000',
    totalTaxFee: '2000',
    totalNetPrice: '18000',
    items: [
      {
        companyName: 'กรมพัฒนาพลังงานทดแทน',
        sumAmount: '10000',
        sumTaxFee: '1000',
        sumNetPrice: '9000',
        detail: [
          {
            status: 'B',
            approveDate: 1592973382,
            companyCode: '12005',
            area: '1000',
            paymentCenter: '1200500001',
            docType: 'KM',
            fiscalYear: '2019',
            accDocNo: '3600011111',
            postDate: 1592973382,
            paymentMethod: '2',
            amount: 3333,
            taxFee: 100,
            netPrice: 3233,
            vendor: 'A120500001',
            vendorName: 'สนง.เลขานุการกรม',
          },
          {
            status: 'B',
            approveDate: 1592973382,
            companyCode: '12005',
            area: '1000',
            paymentCenter: '1200500001',
            docType: 'KM',
            fiscalYear: '2019',
            accDocNo: '3600011111',
            postDate: 1592973382,
            paymentMethod: '2',
            amount: 3333,
            taxFee: 100,
            netPrice: 3233,
            vendor: 'A120500001',
            vendorName: 'สนง.เลขานุการกรม',
          },
          {
            status: 'B',
            approveDate: 1592973382,
            companyCode: '12005',
            area: '1000',
            paymentCenter: '1200500001',
            docType: 'KM',
            fiscalYear: '2019',
            accDocNo: '3600011111',
            postDate: 1592973382,
            paymentMethod: '2',
            amount: 3333,
            taxFee: 100,
            netPrice: 3233,
            vendor: 'A120500001',
            vendorName: 'สนง.เลขานุการกรม',
          },
        ],
      },
    ],
  };

  constructor(
    private paymentReportService: PaymentReportService,
    private route: ActivatedRoute,
    private sidebarService: SidebarService
  ) {
    // this.sidebarService.toggleSidebarVisibility(false);
  }

  ngOnInit() {
    this.route.queryParams.subscribe((params) => {
      if (params.id && params.type) {
      } else {
      }
    });
    // console.log(JSON.stringify(this.vendorReport));
  }
}
