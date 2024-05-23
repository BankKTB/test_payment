import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { interval } from 'rxjs';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { ActivatedRoute } from '@angular/router';
import { PaymentProposalLogService } from '@core/services/payment-proposal-log/payment-proposal-log.service';
import { Paginator } from '@core/models/pagination/paginator';
import { Page } from '@core/models/pagination/page';

@Component({
  selector: 'app-log',
  templateUrl: './log.component.html',
  styleUrls: ['./log.component.scss'],
})
export class LogComponent implements OnInit, OnDestroy {
  @ViewChild('paginatorSuccess', { static: true }) paginatorSuccess: MatPaginator;
  @ViewChild('paginatorError', { static: true }) paginatorError: MatPaginator;
  panelExpanded = true;
  displayedColumns: string[] = [
    'logDate',
    'Time',
    'messageText',
    'messageClass',
    'messageNo',
    'messageType',
  ];
  dataSource = new MatTableDataSource([]);
  dataSourceError = new MatTableDataSource([]);
  subscription = null;
  pageSizeDefault = 5000;
  categoryLog: string = '';
  successPaginator: Paginator = {
    totalElements: 0,
    size: this.pageSizeDefault,
    page: 1,
  };
  errorPaginator: Paginator = {
    totalElements: 0,
    size: this.pageSizeDefault,
    page: 1,
  };

  paymentAliasId: string;
  typeSelected: string = '1';
  overviewAmount = {
    total: 0,
    success: 0,
    error: 0,
  };

  headerText = '';

  constructor(
    private paymentProposalLogService: PaymentProposalLogService,
    private route: ActivatedRoute,
  ) {
  }

  ngOnInit() {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }

    this.route.queryParams.subscribe((params) => {
      this.headerText=''
      if (params.id && params.type) {
        this.categoryLog = params.type;
        this.paymentAliasId = params.id;
        if (params.type === '0') {
          this.searchProposalLog();
          this.headerText = 'แสดงล็อกข้อเสนอ';
        } else if (params.type === '1') {
          this.searchPaymentRealRunLog();
          this.headerText = 'แสดงล็อกการดำเนินการชำระเงิน';
          this.subscription = interval(20000).subscribe(() => {
            this.searchPaymentRealRunLog();
          });
        }
      } else {
      }
    });
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  searchProposalLog() {
    let page = 1;
    let size = 5;
    if (this.typeSelected === '1') {
      page = this.successPaginator.page;
      size = this.successPaginator.size;
    } else {
      page = this.errorPaginator.page;
      size = this.errorPaginator.size;
    }
    this.paymentProposalLogService
      .findProposalLogByPaymentAliasIdPagination(this.paymentAliasId, this.typeSelected, page, size)
      .then((result) => {
        if (result.status === 200) {
          const data = result.data;
          if (data) {
            if (data.content && data.content.length > 0) {
              this.overviewAmount = {
                error: data.content[0].error,
                success: data.content[0].success,
                total: data.content[0].total,
              };
              if (this.typeSelected === '1') {
                this.dataSource = new MatTableDataSource(data.content);
                this.successPaginator = {
                  ...this.successPaginator,
                  totalElements: data.totalElements,
                };
              } else {
                this.dataSourceError = new MatTableDataSource(data.content);
                this.errorPaginator = {
                  ...this.errorPaginator,
                  totalElements: data.totalElements,
                };
              }
            }
          }
        } else if (result.status === 404) {
        }
      });
  }

  searchPaymentRealRunLog() {
    let page = 1;
    let size = 5;
    if (this.typeSelected === '1') {
      page = this.successPaginator.page;
      size = this.successPaginator.size;
    } else {
      page = this.errorPaginator.page;
      size = this.errorPaginator.size;
    }
    this.paymentProposalLogService.findRealRunLogByPaymentAliasIdPagination(this.paymentAliasId, this.typeSelected, page, size).then((result) => {
      console.log(result);
      if (result.status === 200) {
        const data = result.data;
        if (data) {
          this.displayedColumns = ['logDate', 'Time', 'messageText'];
          // this.dataSource = new MatTableDataSource(data);
          if (data.content && data.content.length > 0) {
            this.overviewAmount = {
              error: data.content[0].error,
              success: data.content[0].success,
              total: data.content[0].total,
            };
            if (this.typeSelected === '1') {
              this.dataSource = new MatTableDataSource(data.content);
              this.successPaginator = {
                ...this.successPaginator,
                totalElements: data.totalElements,
              };
            } else {
              this.dataSourceError = new MatTableDataSource(data.content);
              this.errorPaginator = {
                ...this.errorPaginator,
                totalElements: data.totalElements,
              };
            }
          }
        }
      } else if (result.status === 404) {
      }
    });
  }

  tabChanged(e: any) {
    this.typeSelected = (e.index === 0) ? '1' : '0';
    if (this.categoryLog === '0') {
      this.searchProposalLog();
    } else {
      this.searchPaymentRealRunLog();
    }
    this.successPaginator = {
      ...this.successPaginator,
      size: this.pageSizeDefault,
      page: 1,
    };
    this.errorPaginator = {
      ...this.errorPaginator,
      size: this.pageSizeDefault,
      page: 1,
    };
  }

  onPageChange(e: Page) {
    this.successPaginator = {
      ...this.successPaginator,
      page: e.pageIndex + 1,
      size: e.pageSize,
    };
    if (this.categoryLog === '0') {
      this.searchProposalLog();
    } else {
      this.searchPaymentRealRunLog();
    }
  }
}
