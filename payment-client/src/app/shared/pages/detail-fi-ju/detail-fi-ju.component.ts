import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { ActivatedRoute } from '@angular/router';
import { UserProfile } from '@core/models/user-profile';
import { WebInfo } from '@core/models/web-info';
import { LocalStorageService } from '@core/services/local-storage.service';
import { SidebarService } from '@core/services/sidebar.service';

import { APP_DATE_FORMATS, AppDateAdapter } from '@shared/utils/format-datepicker';

import { Utils } from '@shared/utils/utils';
import { Constant } from '@shared/utils/constant';
import { FiService } from '@core/services/fi/fi.service';
import { DialogShowAutodocComponent } from '@shared/component/dialog-show-autodoc/dialog-show-autodoc.component';
import { MatDialog } from '@angular/material/dialog';
import Decimal from 'decimal.js';
import { LoaderService } from '@core/services';
import { DialogTaxFeeComponent } from '@shared/component/dialog-tax-fee/dialog-tax-fee.component';
import { GenerateJuService } from '@core/services/generate-ju/generate-ju.service';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-detail-fi-ju',
  templateUrl: './detail-fi-ju.component.html',
  styleUrls: ['./detail-fi-ju.component.scss'],
})
export class DetailFiJuComponent implements OnInit {
  @ViewChild('tabRef', { static: true }) tabRef: any;
  userProfile: UserProfile;
  private webInfo: WebInfo;
  searchParams: any;
  displayedGroupColumns: string[] = [
    'companyCode',
    'documentNo',
    'fiscalYear',
    'dateAcct',
    'dateDoc',
    'docType',
    'reference',
    'paymentCenter',
    'costCenter',
    'fundSource',
  ];
  displayedColumns: string[] = [
    'line',
    'debitCredit',
    'accountNo',
    // 'fundSource',
    'assignment',
    'bgCode',
    'budgetActivity',
    'amount',
    'amountReceive',
  ];
  displayedSummaryGroupColumns: string[] = ['summaryType', 'summaryCount'];
  dataSources = new MatTableDataSource([]);
  dataSourcesSummaryGroup = new MatTableDataSource([]);
  dataSourcesSummary = new MatTableDataSource([]);
  dateDocHeader = new Date().getTime();
  loadUserHeader = 'GENJUDOC01';
  usernameHeader = '';
  sourceHeader = 'Web Loader';
  summaryAll = {
    summaryDoc: 0,
    summaryAmount: 0,
  };
  tempSearchCriteriaExport: any;
  pathServiceExportExcel: string = '/genJu/getJuDocument/detail/exportExcel';
  pathServiceExportPDF: string = '/genJu/getJuDocument/detail/exportPdf';
  fileNameExportExcel: string = 'รายงานนำส่งแบบฟอร์ม แบบฟอร์มนำส่งสำเร็จ I-Interface J-บช01';
  fileNameExportPDF: string = 'รายงานนำส่งแบบฟอร์ม แบบฟอร์มนำส่งสำเร็จ I-Interface J-บช01';

  constructor(
    public constant: Constant,
    private formBuilder: FormBuilder,
    public utils: Utils,
    private route: ActivatedRoute,
    private localStorageService: LocalStorageService,
    private sidebarService: SidebarService,
    private generateJuService: GenerateJuService
  ) {
    this.route.queryParams.subscribe((params) => {
      if (params.searchParams) {
        // sessionStorage.setItem('searchParams', params.searchParams);
        this.searchParams = JSON.parse(params.searchParams);
        this.tempSearchCriteriaExport = this.searchParams;
        // this.router.navigate(['/fm/fma055/budget-amount'], {});
        // this.utils.convertYearCriteriaToAD(this.searchCriteria, this.columnYearConvert);
      } else {
        // this.searchCriteria = JSON.parse(sessionStorage.getItem('searchParams'));
        // this.utils.convertYearCriteriaToAD(this.searchCriteria, this.columnYearConvert);
      }
      // if (params.head) {
      // this.headId = params.head;
      // }
    });
  }

  ngOnInit() {
    this.sidebarService.toggleSidebarVisibility(false);
    this.getDocumentJuDetail();
    this.userProfile = this.localStorageService.getUserProfile();
    this.webInfo = this.localStorageService.getWebInfo();
    this.usernameHeader = this.userProfile.userdata.username;
  }

  getDocumentJuDetail() {
    if (this.searchParams) {
      this.generateJuService.getJuDocument(this.searchParams).then((response) => {
        if (response.status === 200) {
          let resultData = [];
          let summaryDataGroup = [];
          let summaryData = [];
          const items: any = response.data;
          console.log('items', items);
          if (!!items) {
            const listJuHead = items.listJuHead;
            if (listJuHead.length > 0) {
              listJuHead.forEach((juHeads) => {
                resultData.push({
                  ...juHeads,
                  type: 'HEAD',
                  costCenter: juHeads.list.length > 0 && juHeads.list[0].costCenter,
                  fundSource: juHeads.list.length > 0 && juHeads.list[0].fundSource,
                });
                if (juHeads.list.length > 0) {
                  juHeads.list.forEach((list, index) => {
                    resultData.push({
                      ...list,
                      line: index + 1,
                      debitCredit: 'DR',
                      accountNo: list.glAccountDr,
                      accountName: list.glAccountDrName,
                      budgetActivity: list.mainActivity,
                      amount: list.amountDr,
                      amountReceive: list.amountDr,
                      type: 'ITEM',
                    });
                  });
                  // out CR line
                  resultData.push({
                    type: 'ITEM',
                    line: juHeads.list.length + 1,
                    debitCredit: 'CR',
                    accountNo: juHeads.glAccountCr,
                    accountName: juHeads.glAccountCrName,
                    assignment: juHeads.list[0].assignment,
                    bgCode: juHeads.list[0].bgCode,
                    budgetActivity: juHeads.list[0].mainActivity,
                    amount: juHeads.amountCr,
                    amountReceive: juHeads.amountCr,
                  });
                }
              });
              this.dataSources = new MatTableDataSource(resultData);
            }
            summaryDataGroup.push({
              summaryType: 'รวมจำนวนไฟล์ : ',
              summaryCount: items.summaryFile,
              type: 'GROUP',
            });
            summaryDataGroup.push({
              summaryType: 'รวมจำนวนเอกสาร : ',
              summaryCount: items.summaryDocument,
              type: 'GROUP',
            });
            this.dataSourcesSummaryGroup = new MatTableDataSource(summaryDataGroup);
            summaryData.push({
              summaryType: 'รวมจำนวนไฟล์ทั้งสิ้น : ',
              summaryCount: items.summaryFile,
              type: 'GROUP',
            });
            summaryData.push({
              summaryType: 'รวมจำนวนเอกสารทั้งสิ้น : ',
              summaryCount: items.summaryDocument,
              type: 'GROUP',
            });
            this.dataSourcesSummary = new MatTableDataSource(summaryData);

            this.summaryAll = {
              summaryAmount: items.summaryAmount,
              summaryDoc: items.summaryDocument,
            };
          }
          console.log('getDocumentJuDetail', resultData, summaryDataGroup, summaryData);
        }
      });
    }
  }

  isGroupRow(index, item): boolean {
    if (item.type === 'HEAD') {
      return true;
    } else {
      return false;
    }
  }

  isGroupSummaryRow(index, item): boolean {
    if (item.type === 'GROUP') {
      return true;
    } else {
      return false;
    }
  }
}
