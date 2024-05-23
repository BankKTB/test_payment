import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PaymentReportService } from '@core/services/payment-report/payment-report.service';
import { LocalStorageService } from '@core/services';
import { UserProfile } from '@core/models/user-profile';
import { DialogPaymentRunErrorDetailComponent } from '@shared/component/dialog-payment-run-error-detail/dialog-payment-run-error-detail.component';
import { MatDialog } from '@angular/material/dialog';
import { PaymentRunErrorService } from '@core/services/payment-run-error/payment-run-error.service';
import { PaymentService } from '@core/services/payment/payment.service';
import { MatPaginator } from '@angular/material/paginator';
import { Page } from '@core/models/pagination/page';
import { Paginator } from '@core/models/pagination/paginator';

@Component({
  selector: 'app-display',
  templateUrl: './display.component.html',
  styleUrls: ['./display.component.scss'],
})
export class DisplayComponent implements OnInit {
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  isOpenCollapseDetail;
  vendorReport = null;
  errorReport = null;
  errorReportRealRun = null;
  activePanelIndex = null;
  areaReport = null;
  countryReport: any;
  currencyReport = null;
  paymentReport = null;
  bankReport = null;
  currentDate = new Date();
  type = '';
  userProfile: UserProfile = null;
  paymentAliasId: string;

  companyData: any;
  vendorHeader: any;
  vendors: any[];
  vendorsPagination: Paginator = {
    totalElements: 0,
    size: 5,
    page: 1,
  };
  vendorSelected: string;
  backAccountSelected: string;
  headerText = '';

  constructor(
    private paymentReportService: PaymentReportService,
    private route: ActivatedRoute,
    private localStorageService: LocalStorageService,
    private dialog: MatDialog,
    private paymentRunErrorService: PaymentRunErrorService,
    private paymentService: PaymentService
  ) {}

  ngOnInit() {
    this.userProfile = this.localStorageService.getUserProfile();

    this.route.queryParams.subscribe((params) => {
      this.headerText = '';
      if (params.id && params.type) {
        this.paymentAliasId = params.id;
        let type = '';
        if (params.type === '1' || params.type === '2') {
          type = '1';
          this.headerText = 'รายการชำระเงิน';
        } else {
          this.headerText = 'รายการข้อเสนอ';
          type = '0';
        }
        this.searchPaymentReportCompanyCode().then(() => {
          this.searchVendorReport(params.id, type).then(() => {
            this.searchErrorReport(params.id, type);
            this.searchAreaWithPaymentReport(params.id, type);
            this.searchCountryWithPaymentReport(params.id, type);
            this.searchCurrencyWithPaymentReport(params.id, type);
            this.searchPaymentMethodReport(params.id, type);
            this.searchBankReport(params.id, type);
          });
        });
        if (params.type === '1') {
          this.type = '2';
        } else {
          this.type = params.type;
        }
      } else {
      }
    });
  }

  async searchPaymentReportCompanyCode() {
    await this.paymentReportService
      .findPaymentReportCompanyCode(this.paymentAliasId)
      .then((result) => {
        if (result.status === 200) {
          const data = result.data;
          if (data) {
            this.companyData = data;
          }
        } else if (result.status === 404) {
        }
      });
  }

  async searchVendorReport(id, type) {
    await this.paymentReportService.findVendorReportByAlias(id, type).then((result) => {
      if (result.status === 200) {
        console.log('findVendorReportByAlias; ', result);
        const data = result.data;
        if (data) {
          this.vendorReport = data;
        }
      } else if (result.status === 404) {
      }
    });
  }

  searchErrorReport(id, type) {
    if (type === '0') {
      this.paymentReportService.findErrorReportByAlias(id, type).then((result) => {
        if (result.status === 200) {
          const data = result.data;
          if (data) {
            this.errorReport = data;
          }
        } else if (result.status === 404) {
        }
      });
    } else {
      this.paymentReportService.findErrorDetailByPaymentAliasId(id).then((result) => {
        if (result.status === 200) {
          const data = result.data;
          if (data) {
            this.errorReportRealRun = data;
          }
        } else if (result.status === 404) {
        }
      });
    }
  }

  searchAreaWithPaymentReport(id, type) {
    this.paymentReportService.findAreaWithPaymentMethodReportByAlias(id, type).then((result) => {
      if (result.status === 200) {
        const data = result.data;
        if (data) {
          this.areaReport = data;
        }
      } else if (result.status === 404) {
      }
    });
  }

  searchCountryWithPaymentReport(id, type) {
    this.paymentReportService.findCountryWithPaymentMethodReportByAlias(id, type).then((result) => {
      if (result.status === 200) {
        const data = result.data;
        if (data) {
          this.countryReport = data;
        }
      } else if (result.status === 404) {
      }
    });
  }

  searchCurrencyWithPaymentReport(id, type) {
    this.paymentReportService
      .findCurrencyWithPaymentMethodReportByAlias(id, type)
      .then((result) => {
        if (result.status === 200) {
          const data = result.data;
          if (data) {
            this.currencyReport = data;
          }
        } else if (result.status === 404) {
        }
      });
  }

  searchPaymentMethodReport(id, type) {
    this.paymentReportService.findPaymentMethodReportByAlias(id, type).then((result) => {
      if (result.status === 200) {
        const data = result.data;
        if (data) {
          this.paymentReport = data;
        }
      } else if (result.status === 404) {
      }
    });
  }

  searchBankReport(id, type) {
    this.paymentReportService.findBankReportByAlias(id, type).then((result) => {
      if (result.status === 200) {
        const data = result.data;
        if (data) {
          this.bankReport = data;
        }
      } else if (result.status === 404) {
      }
    });
  }

  viewDocument(item) {
    if (this.type === '2') {
      const companyCode = item.paymentCompanyCode;
      const paymentDocNo = item.paymentDocumentNo;
      const fiscalYear = item.paymentFiscalYear;

      const url =
        './detail-fi-kb?compCode=' +
        companyCode +
        '&docNo=' +
        paymentDocNo +
        '&docYear=' +
        fiscalYear;
      // window.open(url, 'name', 'width=1200,height=700');
      window.open(url, '_blank');
    }
  }

  openDialogPaymentRunErrorDetail(document) {
    if (this.type === '2') {
      this.paymentRunErrorService
        .findErrorDetailByInvoice(
          document.code2,
          document.invoiceDocumentNo,
          document.invoiceFiscalYear
        )
        .then((data) => {
          const response = data as any;
          if (response.status === 200) {
            const value = response.data;
            const dialogRef = this.dialog.open(DialogPaymentRunErrorDetailComponent, {
              width: '1200px',
              data: {
                value,
              },
            });
            dialogRef.afterClosed().subscribe((result) => {
              if (result) {
              }
            });
          }
        });
    }
  }

  repairDocument(document) {
    if (this.type === '2') {
      this.paymentService
        .repairDocument(document.paymentProcessId, this.localStorageService.getWebInfo())
        .then((data) => {
          const response = data as any;
        });
    }
  }

  onClickPanel(vendorIndex: number, vendor: any) {
    this.resetSelectedVendor().then(() => {
      if (this.activePanelIndex === null) {
        this.activePanelIndex = vendorIndex;
      } else {
        if (vendorIndex === this.activePanelIndex) {
          this.activePanelIndex = null;
          return;
        }
        this.activePanelIndex = vendorIndex;
      }
      this.vendorSelected = vendor.vendorCode;
      this.backAccountSelected = vendor.bankAccountNo;
      this.findVendor();
    });
  }

  onPageChange(e: Page) {
    //TODO CONNECT API PAGINATION
    console.log('onPageChange', e);
    this.vendorsPagination = {
      ...this.vendorsPagination,
      page: e.pageIndex + 1,
      size: e.pageSize,
    };
    this.findVendor();
  }

  findVendor() {
    const item = {
      paymentAliasId: this.paymentAliasId,
      vendor: this.vendorSelected,
      backAccount: this.backAccountSelected,
    };
    console.log('findVendor', item);
    this.paymentReportService
      .findOneVendor(item, this.type, this.vendorsPagination.page, this.vendorsPagination.size)
      .then((result) => {
        console.log('findOneVendor', result);
        if (result && result.status === 200) {
          const response = result.data;
          if (response && response.content.length > 0) {
            this.vendorHeader = response.content[0];
            this.vendors = response.content;

            this.vendorsPagination = {
              ...this.vendorsPagination,
              totalElements: response.totalElements,
            };

            setTimeout(() => {
              this.vendors.map((e) => {
                let invoiceAmountPaidChild: number = 0;
                let invoiceAmountChild: number = 0;
                let invoiceWtxAmountChild: number = 0;

                if (e.paymentReportListChild && e.paymentReportListChild.length > 0) {
                  invoiceAmountPaidChild = e.paymentReportListChild
                    .map((a) => a.invoiceAmountPaid)
                    .reduce(function (a, b) {
                      return a + b;
                    });
                  invoiceAmountChild = e.paymentReportListChild
                    .map((a) => a.invoiceAmount)
                    .reduce(function (a, b) {
                      return a + b;
                    });
                  invoiceWtxAmountChild = e.paymentReportListChild
                    .map((a) => a.invoiceWtxAmount)
                    .reduce(function (a, b) {
                      return a + b;
                    });
                }

                e.amountPay = e.invoiceAmountPaid - invoiceAmountPaidChild;
                e.summaryAmountSumFc = e.invoiceAmount - invoiceAmountChild;
                e.summaryAmountMinusFc = e.invoiceWtxAmount - invoiceWtxAmountChild;
                e.summaryAmountNet = e.invoiceAmountPaid - invoiceAmountPaidChild;
              });
            }, 500);
          }
        } else {
          this.activePanelIndex = null;
          alert('ไม่สามารถเรียกข้อมูลได้');
        }
      });
  }

  async resetSelectedVendor() {
    this.vendorsPagination = {
      totalElements: 0,
      size: 5,
      page: 1,
    };
    this.vendorSelected = '';
    this.backAccountSelected = '';
  }
}
