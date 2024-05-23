import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { ActivatedRoute, NavigationEnd, Router, RouterEvent } from '@angular/router';
import { filter } from 'rxjs/operators';

import { DatepickerHeaderComponent } from '@shared/component/datepicker-header/datepicker-header.component';
import { Constant } from '@shared/utils/constant';
import { WebInfo } from '@core/models/web-info';
import { BrService } from '@core/services/br/br.service';
import { LoaderService, LocalStorageService } from '@core/services';
import { ReportService } from '@core/services/report/report-service';
import { MasterService } from '@core/services/master/master.service';
import { SidebarService } from '@core/services/sidebar.service';
import Decimal from 'decimal.js';
import { MatDialog } from '@angular/material/dialog';
import { MatTabChangeEvent } from '@angular/material/tabs';
import { Utils } from '@shared/utils/utils';
import { FiService } from '@core/services/fi/fi.service';

@Component({
  selector: 'app-detail-sng',
  templateUrl: './detail-sng.component.html',
  styleUrls: ['./detail-sng.component.scss'],
  providers: [],
})
export class DetailSngComponent implements OnInit {
  @ViewChild('tabRef', { static: true }) tabRef: any;
  sortBy = 'เลขที่เอกสาร'; // not sure api order by
  orderBy = 'ASC'; // 1=ASC 0=DESC
  tabAmount: number;
  listDocumentShowRef = [];

  listValidate = []; //
  listDocument = [];
  selectListOrder = null; // เลือกหน้ารายการบัญชีที่เลือกจากตาราง
  isSelectOrder = false; // check is order select
  isBtnDelete = true; // เช็คปุ่มdelete
  isBtnNew = true; // เช็คปุ่มสร้างรายการใหม่
  isSubmitedForm = false;
  isDisabledFromSearch = false; // disabled all filde all from search
  isOpenCollapseDetail = true;
  isDisableShowRefData = false;
  isDisableRef = false;
  isDataRef = false;
  checkHideTab = false;
  checkHideKeepOrder = false;
  checkTypeWtx = '';
  checkTypeVendorGroup = '';
  listFormControlValidate: any;

  isDisableCancel = false;
  isDisableCancelReason = false;
  isDisableConsumption = false;
  brDocNo = '';
  // sumAcAmountCollapsed = false;

  brAmount = null;
  acAmount = [];
  poAmount = [];
  brAdjust = [];
  brClosed = null;
  documentDate = null;
  departmentCode = null;
  disbursementCode = null;
  postDate = null;
  documentType = null;
  amountMoney = null;
  amountArrearsMoney = null;
  yearSourceMoney = null;
  sourceMoneyCode = null;
  sourceBudgetCode = null;
  areaCode = null;
  budgetAccount = null;
  reference2 = null;
  headerText = null;
  reasonCode = null;
  reasonDescription = null;
  cancelReason = null;
  localName = null;
  contractNo = null;
  lcNo = null;
  description = null;
  vendorName = null;
  signDate = null;
  dueDate = null;

  sumBrAdjustCollapsed = false;
  sumPoAmountCollapsed = false;
  sumAcAmountCollapsed = false;

  sumAcAmount = null;
  sumPoAmount = null;
  sumBrAdjust = null;

  sumAmountUsed = new Decimal(0);
  sumAmountOutstanding = new Decimal(0);
  sumAmountReferences = new Decimal(0);
  divisionBindingName = '';
  // FI BC
  typeAccountCodeBindingName = ''; // รหัสบัญชีแยกประเภท
  subBookOptional = ''; // สีของsub Book
  subTypeAccountCodeBindingName = ''; // รหัส subBook
  centerCodeBindingName = ''; // รหัสศูนย์ต้นทุน
  sourceMoneyCodeBindindName = ''; // รหัสแหล่งของเงิน
  sourceBudgetCodeBindingName = ''; // รหัสงบประมาณ
  mainActivityCodeBindingName = ''; // รหัสกิจกรรมหลัก
  subActivityCodeBindingName = ''; // รหัสกิจกรรมย่อย
  accountDepositCodeBindingName = ''; // รหัสบัญชีเงินฝากคลัง
  ownAccountDepositCodeBindingName = ''; // รหัสเจ้าของบัญชีเงินฝากคลัง
  subAccountCodeBindingName = ''; // รหัสบัญชีย่อย
  ownSubAccountCodeBindingName = ''; // รหัสเจ้าของบัญชีย่อย
  bankBookBidingName = ''; // รหัสบัญชีธนาคารย่อย (Bank Book)
  packageCodeBindingName = ''; // รหัสหมวดพัสดุ
  incomeBindingName = ''; // รหัสรายได้
  // FI
  sideListBindingName = ''; // ด้านรายการ
  // FI KB
  vendorTaxIdBindingName; // เลขประจำตัวบัตรประชาชน /เลขประจำตัวผู้เสียภาษี
  vendorTaxId2BindingName; // เลขประจำตัวบัตรประชาชน /เลขประจำตัวผู้เสียภาษี  โอนสิทธิ
  // FI NS
  storageCenterCodeBindingName = ''; // รหัสศูนย์ต้นทุนผู้จัดเก็บ
  remittanceCenterCodeBindingName = ''; // รหัสศูนย์ต้นทุนผู้นำส่ง
  ownCenterCodeBindingName = ''; // รหัสศูนย์ต้นทุนเจ้าของรายได้
  // VD PK
  bankBranchBindingName = ''; // ชื่อสาขาของธนาคาร
  // PO BS
  gpscCodeBindingName = ''; // รหัส GPSC
  // FA ST
  assetCodeBindingName = ''; // หมวดสินทรัพย์
  uomBindingName = ''; // หน่วย
  // AreaCodeCenterCode
  areaCodeCenterCode = ''; // รหัสพื้นที่ศูนย์ต้นทุน
  areaCodeBindingName = '';
  compCodeBindingName = ''; //รหัสหน่วยงาน
  createBy = '';
  docNoFromSearchDetail = '';
  docStatus = '';
  documentNo = 1; // เลขลำดับ
  tabSelectedIndex = 0;
  recordId: '';
  p = 1;

  doctypeName: string;
  headerTemp = null;
  public datePickerHeader = DatepickerHeaderComponent;
  private webInfo: WebInfo;

  constructor(
    public constant: Constant,
    public utils: Utils,
    private loaderService: LoaderService,
    private formBuilder: FormBuilder,
    private localStorageService: LocalStorageService,
    private route: ActivatedRoute,
    private dialog: MatDialog,
    private brService: BrService,
    private masterService: MasterService,
    private router: Router,
    private reportService: ReportService,
    private sidebarService: SidebarService,
  ) {
  }

  ngOnInit() {
    window.scrollTo(0, 0);
    this.webInfo = this.localStorageService.getWebInfo();
    this.router.events.pipe(filter((event: RouterEvent) => event instanceof NavigationEnd)).subscribe(() => {
    });
    this.sidebarService.toggleSidebarVisibility(false);
    this.webInfo = this.localStorageService.getWebInfo();

    this.route.queryParams.subscribe((params) => {
      if (params.docNo) {
        this.docNoFromSearchDetail = params.docNo;
        this.recordId = params.recordId;
        this.searchDocumentDetail(params.docNo, params.recordId, params.companyCode, params.paymentCenter, params.formID);
      }
    });
  }

  cancelDocumentFromSearch() {
    this.router.navigate(['sng01/sng013'], {
      queryParams: { docNo: this.docNoFromSearchDetail },
    });
  }


  setInputFormSelect(item) {
    this.incomeBindingName = item.incomeName;
    // this.areaCodeCenterCode = item.areaCodeCenterCode
    // this.sourceMoneyCodeBindindName = item.sourceMoneyName;
    // this.sourceBudgetCodeBindingName = item.sourceBudgetName;
    // this.areaCodeBindingName = item.areaCodeName;
    this.departmentCode = item.departmentCode;
    this.documentDate = item.documentDate;
    this.disbursementCode = item.disbursementCode;
    this.postDate = item.postDate;
    this.documentType = item.documentType;
    this.amountMoney = item.amountMoney;
    this.amountArrearsMoney = item.amountArrearsMoney;
    this.yearSourceMoney = item.yearSourceMoney;
    this.sourceMoneyCode = item.sourceMoneyCode;
    this.sourceBudgetCode = item.sourceBudgetCode;
    this.areaCode = item.areaCode;
    this.budgetAccount = item.budgetAccount;
    this.reference2 = item.reference2;
    this.headerText = item.headerText;
    this.reasonCode = item.reasonCode;
    this.reasonDescription = item.reasonDescription;
    this.localName = item.localName;
    this.contractNo = item.contractNo;
    this.lcNo = item.lcNo;
    this.description = item.description;
    this.vendorName = item.vendorName;
    this.signDate = item.signDate;
    this.dueDate = item.dueDate;
  }

  setInputFromSearchToForm(head, items, contracts, references) {
    console.log(items);
    console.log(head);
    const dateDoc = new Date(head.dateDoc);
    const postDoc = new Date(head.dateAcct);
    const item = items[0] as any;
    this.sourceMoneyCodeBindindName = item.fundSourceName;
    this.sourceBudgetCodeBindingName = item.budgetCodeName;
    this.areaCodeBindingName = item.budgetAreaName;
    this.compCodeBindingName = head.companyCodeName;
    this.divisionBindingName = head.paymentCenterName;
    const contract = contracts !== undefined && contracts.length > 0 ? contracts[0] : ({} as any);
    const signDate = new Date(contract.signDate);
    const dueDate = new Date(contract.dueDate);
    this.brDocNo = head.reservationDocumentNo;
    this.departmentCode = head.companyCode;
    this.documentDate = head.dateDoc;
    this.disbursementCode = item.paymentCenter;
    this.postDate = head.dateAcct;
    this.documentType = head.docType + ' - ' + head.docTypeName;
    this.amountMoney = item.amount;
    this.amountArrearsMoney = item.openAmount;
    // yearSourceMoney: Number(this.utils.displayYearSourceMoneyBySourceMoneyCode(item.fundSource)),
    this.sourceMoneyCode = item.fundSource;
    this.sourceBudgetCode = item.budgetCode;
    this.areaCode = item.budgetArea;
    this.budgetAccount = item.budgetAccount;
    this.reference2 = head.reference2;
    this.headerText = head.headerText;
    this.reasonCode = head.reasonNo + ' - ' + head.reasonName;
    this.reasonDescription = head.reasonDescription;
    this.cancelReason = head.cancelReason;
    this.localName = contract.localName;
    this.contractNo = contract.contractNo;
    this.lcNo = contract.lcNo;
    this.description = contract.description;
    this.vendorName = contract.vendor;
    this.signDate = contract.signDate;
    this.dueDate = contract.dueDate;
    if (references) {
      this.listDocumentShowRef = references;
      console.log(this.listDocumentShowRef);
      this.isDisableRef = false;
      this.listDocumentShowRef.forEach((element) => {
        const amount = new Decimal(element.amount);
        this.sumAmountReferences = this.sumAmountReferences.plus(amount);
      });
    } else {
      this.isDataRef = true;
    }
    this.documentDate = head.dateDoc;

    this.createBy = head.userWebOnline;
    this.docStatus = head.docStatus === 'C' ? 'ยกเลิกแล้ว' : head.docStatus === 'B' ? ' Block' : 'ยังไม่ยกเลิก';
    this.listDocument = [];
    if (head.docStatus === 'C' || head.docStatus === 'B') {
      this.isDisableCancel = true;
    }
    if (contracts) {
      if (contracts.length > 0) {
        for (const contrac of contracts) {
          const data = {
            departmentCode: head.companyCode,
            documentDate: dateDoc,
            disbursementCode: item.paymentCenter,
            postDate: postDoc,
            documentType: head.docType,
            amountMoney: item.amount,
            amountArrearsMoney: item.openAmount,
            // yearSourceMoney: Number(this.utils.displayYearSourceMoneyBySourceMoneyCode(item.fundSource)),
            sourceMoneyCode: item.fundSource,
            sourceBudgetCode: item.budgetCode,
            areaCode: item.budgetArea,
            budgetAccount: item.budgetAccount,
            reference2: head.reference2,
            headerText: head.headerText,
            reasonCode: head.reasonNo,
            reasonDescription: head.reasonDescription,

            localName: contrac.localName,
            contractNo: contrac.contractNo,
            lcNo: contrac.lcNo,
            description: contrac.description,
            vendorName: contrac.vendor,
            signDate: new Date(contrac.signDate),
            dueDate: new Date(contrac.dueDate),
          };

          this.listDocument.push(data);
        }
      }
    } else {
      this.signDate = contract.signDate;
      this.dueDate = contract.dueDate;
      // for (let i = 0; i < items.length; i++) {
      //   const data = {
      //     departmentCode: head.compCode,
      //     documentDate: dateDoc,
      //     disbursementCode: items[i].paymentCenter,
      //     postDate: postDoc,
      //     documentType: head.docType,
      //     amountMoney: items[i].amount,
      //     amountArrearsMoney: items[i].openAmount,
      //     yearSourceMoney: Number(this.utils.displayYearSourceMoneyBySourceMoneyCode(item.fundSource)),
      //     sourceMoneyCode: items[i].fundSource,
      //     sourceBudgetCode: items[i].bgCode,
      //     areaCode: items[i].fiArea,
      //     budgetAccount: items[i].bgAccount,
      //     reference2: head.reference2,
      //     headerText: head.headText,
      //     reasonCode: head.reasonNo,
      //     reasonDescription: head.reasonDescription,

      //     localName: '',
      //     contractNo: '',
      //     lcNo: '',
      //     description: '',
      //     vendorName: '',
      //     signDate: '',
      //     dueDate: '',
      //   };

      //   this.listDocument.push(data);
      // }
    }
    console.log(this.listDocument);

    this.searchConsumptionDocument(this.brDocNo, head.brRecordID, this.departmentCode, this.disbursementCode);
  }

  searchDocumentDetail(docNo, recordId, companyCode, paymentCenter, formId?) {
    const webInfo = {
      fiArea: this.localStorageService.getWebInfo().fiArea,
      compCode: companyCode,
      ipAddress: this.localStorageService.getWebInfo().userWebOnline,
      paymentCenter,
      userWebOnline: this.localStorageService.getWebInfo().userWebOnline,
      authPaymentCenter: this.localStorageService.getWebInfo().authPaymentCenter,
      authCostCenter: this.localStorageService.getWebInfo().authCostCenter,
      authFIArea: this.localStorageService.getWebInfo().authFIArea,
      authCompanyCode: this.localStorageService.getWebInfo().authCompanyCode,
    };
    const payload = {
      brDocumentNo: docNo,
      // brRecordID: recordId,
      companyCode,
      paymentCenter,
      formID: 'F01',
      webInfo,
    };
    this.brService.searchDetail(payload).then((result) => {
      if (result.status === 200) {
        const header = result.data.header as any;
        this.headerTemp = header;
        const items = result.data.lines as any;
        const contracts = result.data.contracts as any;
        const references = result.data.references as any;
        console.log(result);
        this.setInputFromSearchToForm(header, items, contracts, references);
        this.hideTab(header.docType);
        this.tabSelectedIndex = 0;
        this.isDisabledFromSearch = true;
      } else {
        // TODO
      }
    });
  }

  // clickSelectOrderList(type) {
  //   if (type === 'prev') {
  //     this.selectListOrder = this.selectListOrder - 1;
  //     if (this.selectListOrder % this.constant.PER_PAGINATION === 0) {
  //       this.p -= 1;
  //     }
  //   } else {
  //     this.selectListOrder = this.selectListOrder + 1;
  //     if (this.selectListOrder % this.constant.PER_PAGINATION === 1) {
  //       this.p += 1;
  //     }
  //   }
  //   this.selectOrder(this.listDocument[this.selectListOrder - 1], this.selectListOrder - 1);
  // }

  checkTab(type) {
    if (type === 'prev') {
      this.tabSelectedIndex = this.tabSelectedIndex - 1;
    } else {
      this.tabSelectedIndex = this.tabSelectedIndex + 1;
    }
  }

  selectOrder(item, i) {
    this.selectListOrder = i + 1;
    this.documentNo = i + 1;
    this.setInputFormSelect(item);
    this.isBtnDelete = false;
    this.isBtnNew = false;
    this.isSelectOrder = true;
  }

  openCollapseDetail() {
    this.isOpenCollapseDetail = !this.isOpenCollapseDetail;
  }

  tabChanged(tabChangeEvent: MatTabChangeEvent): void {
    this.tabSelectedIndex = tabChangeEvent.index;
  }

  sortData(sortType) {
    this.orderBy = this.orderBy === 'ASC' ? 'DESC' : 'ASC';
    if (sortType === 'accDocNo') {
      this.sortBy = 'เลขที่เอกสาร';
      if (this.orderBy === 'ASC') {
        this.listDocument.sort((a, b) => (a.accDocNo > b.accDocNo ? 1 : -1));
      } else {
        this.listDocument.sort((a, b) => (b.accDocNo > a.accDocNo ? 1 : -1));
      }
    } else if (sortType === 'fiscYear') {
      this.sortBy = 'ปีงบประมาณ';
      if (this.orderBy === 'ASC') {
        this.listDocument.sort((a, b) => (a.fiscYear > b.fiscYear ? 1 : -1));
      } else {
        this.listDocument.sort((a, b) => (b.fiscYear > a.fiscYear ? 1 : -1));
      }
    } else if (sortType === 'DOC_TYPE') {
      this.sortBy = 'ประเภทเอกสาร';
      if (this.orderBy === 'ASC') {
        this.listDocument.sort((a, b) => (a.DOC_TYPE > b.DOC_TYPE ? 1 : -1));
      } else {
        this.listDocument.sort((a, b) => (b.DOC_TYPE > a.DOC_TYPE ? 1 : -1));
      }
    } else if (sortType === 'paymentMethod') {
      this.sortBy = 'วิธีการชำระเงิน';
      if (this.orderBy === 'ASC') {
        this.listDocument.sort((a, b) => (a.paymentMethod > b.paymentMethod ? 1 : -1));
      } else {
        this.listDocument.sort((a, b) => (b.paymentMethod > a.paymentMethod ? 1 : -1));
      }
    } else if (sortType === 'amount') {
      this.sortBy = 'จำนวนเงินขอเบิก';
      if (this.orderBy === 'ASC') {
        this.listDocument.sort((a, b) => (a.amount > b.amount ? 1 : -1));
      } else {
        this.listDocument.sort((a, b) => (b.amount > a.amount ? 1 : -1));
      }
    }
  }

  hideTab(data) {
    console.log(data);
    if (data === 'CX' || data === 'CK') {
      this.checkHideTab = false;
      // this.listDocument=[]
      // this.checkHideKeepOrder = true;
    } else {
      this.checkHideTab = true;
      // this.listDocument = [];
      // this.checkHideKeepOrder = true;
    }
    // this.checkDoctypeForReason(data)
  }

  searchConsumptionDocument(docNo, brRecordID, companyCode, paymentCenter) {
    const payload = {
      reservationDocumentNo: docNo,
      reservationRecordID: brRecordID,
      formID: 'F01',
      companyCode,
      paymentCenter,
      webInfo: this.localStorageService.getWebInfo(),
    };
    console.log(payload);
    this.acAmount = [];
    this.brService.consumption(payload).then((response) => {
      if (response.status === 200) {
        const result = response.data.group as any;
        console.log(result);
        // this.brAmount = result.brs.find((item) => item.consumeStatus === '10' && item.consumeType === 'BR');
        console.log(this.brAmount);

        this.poAmount = result.pos;

        result.acs.forEach((items) => {
          this.acAmount.push(items);
        });

        result.jvs.forEach((items) => {
          this.acAmount.push(items);
        });

        // this.brAdjust = result.brs.filter((item) => item.comsumeStatus !== '10' && item.comsumeStatus !== '40' && item.comsumeStatus !== '51' && item.comsumeStatus !== '52',
        // );
        result.brs.forEach((items) => {
          if (items.consumeStatus === '10') {
            this.brAmount = items;
          } else if (
            items.consumeStatus !== '10' &&
            items.consumeStatus !== '40' &&
            items.consumeStatus !== '51' &&
            items.consumeStatus !== '52'
          ) {
            this.brAdjust.push(items);
          } else if (items.consumeStatus === '40') {
            this.brClosed = items;
          }
        });
        console.log(this.brAdjust);

        if (this.acAmount) {
          let amount = new Decimal(0);
          this.acAmount.forEach((item) => {
            if (item.amount) {
              const itemAmount = new Decimal(item.amount);
              amount = amount.plus(itemAmount);
            }
          });
          this.sumAcAmount = amount;
        }
        if (this.poAmount) {
          let amount = new Decimal(0);
          this.poAmount.forEach((item) => {
            if (item.amount) {
              const itemAmount = new Decimal(item.amount);
              amount = amount.plus(itemAmount);
            }
          });
          this.sumPoAmount = amount;
        }
        if (this.brAdjust) {
          let amount = new Decimal(0);
          this.brAdjust.forEach((item) => {
            if (item.amount) {
              const itemAmount = new Decimal(item.amount);
              amount = amount.plus(itemAmount);
            }
          });
          this.sumBrAdjust = amount;
        }
        if (!this.brClosed) {
          this.brClosed = {
            amount: new Decimal(0),
          };
        }

        this.sumAmountUsed = this.sumAcAmount.plus(this.sumPoAmount.plus(this.sumBrAdjust));
        if (this.brAmount && this.brClosed) {
          this.brAmount.amount = new Decimal(this.brAmount.amount);
          this.sumAmountOutstanding = this.brAmount.amount.plus(this.sumAmountUsed.plus(this.brClosed.amount));
        }
      } else {
        // TODO
      }
    });
  }

  ngAfterViewInit(): void {
    if (this.tabRef) {
      this.tabAmount = this.tabRef._tabs.length;
    }

    this.isDisableConsumption = false;
  }

  showConsumption() {
    this.isDisableConsumption = true;
    this.isDisableShowRefData = false;
    this.isDisableRef = false;
  }

  showDocument() {
    this.isDisableConsumption = false;
    this.isDisableShowRefData = false;
    this.isDisableRef = false;
  }
}
