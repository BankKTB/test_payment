import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Utils } from '@shared/utils/utils';
import { MatSnackBar } from '@angular/material/snack-bar';
import { LocalStorageService, SidebarService } from '@core/services';
import { MatDialog } from '@angular/material/dialog';
import { GenerateFileService } from '@core/services/generate-file/generate-file.service';
// tslint:disable-next-line:max-line-length
import { DialogSearchParameterGenerateFileComponent } from '@shared/component/dialog-search-parameter-generate-file/dialog-search-parameter-generate-file.component';
// tslint:disable-next-line:max-line-length
import { DialogConfirmComponent } from '@shared/component/dialog-confirm/dialog-confirm.component';
import { MatTableDataSource } from '@angular/material/table';
import { DialogReturnParameterGenFileComponent } from '@shared/component/dialog-return-parameter-gen-file/dialog-return-parameter-gen-file.component';
import { GenerateJuService } from '@core/services/generate-ju/generate-ju.service';
import { DialogOmSearchCriteriaComponent } from '@shared/component/dialog-om-search-criteria/dialog-om-search-criteria.component';
import { UserProfile } from '@core/models/user-profile';
import { DialogOmSaveSearchCriteriaComponent } from '@shared/component/dialog-om-save-search-criteria/dialog-om-save-search-criteria.component';

@Component({
  selector: 'app-search-criteria',
  templateUrl: './search-criteria.component.html',
  styleUrls: ['./search-criteria.component.scss'],
})
export class SearchCriteriaComponent implements OnInit {
  generateJuForm: FormGroup;
  isSubmitedForm = false;
  listPaymentName = [{ from: null, to: null, optionExclude: false }];
  listPaymentDate = [{ from: null, to: null, optionExclude: false }];
  listValidate = [];
  isSearch = false;
  dataSources = new MatTableDataSource([]);
  displayedColumnsGroup: string[] = ['detailLine', 'detailDocument'];
  displayedColumns: string[] = [
    'payAccount',
    'refRunning',
    'refLine',
    'accountNoFrom',
    'accountNoTo',
    'fileType',
    'fileName',
    'amountDr',
    'drCr',
    'glAccountDr',
  ];
  headerColumns: string[] = ['paymentName', 'compCode'];
  summaryColumns: string[] = ['textSum', 'amountSum', 'drCrSum', 'accountNoSum'];
  listGlAccount = [];
  mapValidate = new Map<string, string>();
  juHeadId: string = '';
  userProfile: UserProfile = null;
  role: string = 'GENERATE_JU';
  dataCriteria: any;
  tempSearchCriteriaExport: any;
  pathServiceExportExcel: string = '/genJu/document/exportExcel';
  pathServiceExportPDF: string = '/genJu/document/exportPdf';
  fileNameExportExcel: string = 'รายงานสรุปการโอนเงินจากบัญชี TR2 และสร้างประเภทเอกสาร JU';
  fileNameExportPDF: string = 'รายงานสรุปการโอนเงินจากบัญชี TR2 และสร้างประเภทเอกสาร JU';

  constructor(
    private dialog: MatDialog,
    private router: Router,
    private utils: Utils,
    private formBuilder: FormBuilder,
    private generateFileService: GenerateFileService,
    private generateJuService: GenerateJuService,
    private snackBar: MatSnackBar,
    private sidebarService: SidebarService,
    private localStorageService: LocalStorageService
  ) {
    this.userProfile = this.localStorageService.getUserProfile();
  }

  ngOnInit() {
    this.sidebarService.updatePageType('ju');
    this.sidebarService.updateNowPage('ju');
    this.createFormGroup();
    this.initMapValidateField();
    this.getListGlAccount();
    this.defaultPatchValue();
  }

  createFormGroup() {
    this.generateJuForm = this.formBuilder.group({
      // เงื่อนไขการเลือกข้อมูล
      transferDate: this.formBuilder.control(new Date(), [Validators.required]), // วันที่สั่งโอน [GOVERNMENT ]
      paymentDateFrom: this.formBuilder.control(''), // วันที่ประมวลผล
      paymentDateTo: this.formBuilder.control(''),
      paymentNameFrom: this.formBuilder.control(''), // การกำหนด
      paymentNameTo: this.formBuilder.control(''),
      isTestGenerate: this.formBuilder.control(true), // ทดสอบ
      // เงื่อนไขการสร้างเอกสาร
      compCode: this.formBuilder.control('', [Validators.required]), // รหัสหน่วยงาน
      documentType: this.formBuilder.control('', [Validators.required]), // ประเภทเอกสาร
      glCredit: this.formBuilder.control('', [Validators.required]), // บัญชี GL Credit
      keyDebit: this.formBuilder.control('', [Validators.required]), // คีย์ผ่านรายการ Debit
      keyCredit: this.formBuilder.control('', [Validators.required]), // คีย์ผ่านรายการ Credit
      fundSource: this.formBuilder.control('', [Validators.required]), // แหล่งของเงิน
      costCenter: this.formBuilder.control('', [Validators.required]), // ศูนย์ต้นทุน
      budgetCode: this.formBuilder.control('', [Validators.required]), // รหัสงบประมาณ
      fiArea: this.formBuilder.control('', [Validators.required]), // รหัสจังหวัด
      mainActivity: this.formBuilder.control('', [Validators.required]), // กิจกรรมหลัก
      paymentCenter: this.formBuilder.control('', [Validators.required]), // หน่วยเบิกจ่าย
    });
    // this.generateJuForm.controls.compCode.disable();
    // this.generateJuForm.controls.documentType.disable();
    // this.generateJuForm.controls.glCredit.disable();
    // this.generateJuForm.controls.keyDebit.disable();
    // this.generateJuForm.controls.keyCredit.disable();
    // this.generateJuForm.controls.fundSource.disable();
    // this.generateJuForm.controls.costCenter.disable();
    // this.generateJuForm.controls.budgetCode.disable();
    // this.generateJuForm.controls.fiArea.disable();
    // this.generateJuForm.controls.mainActivity.disable();
    // this.generateJuForm.controls.paymentCenter.disable();
  }

  initMapValidateField() {
    this.mapValidate.set('transferDate', 'กรุณาระบุ วันที่สั่งโอน');
    this.mapValidate.set('compCode', 'กรุณาระบุ รหัสหน่วยงาน');
    this.mapValidate.set('documentType', 'กรุณาระบุ ประเภทเอกสาร');
    this.mapValidate.set('glAccountD1', 'กรุณาระบุ บัญชี GL บัญชีจ่าย D1');
    this.mapValidate.set('glAccountD2', 'กรุณาระบุ บัญชี GL บัญชีจ่าย D2');
    this.mapValidate.set('glAccountD3', 'กรุณาระบุ บัญชี GL บัญชีจ่าย D3');
    this.mapValidate.set('glAccountD4', 'กรุณาระบุ บัญชี GL บัญชีจ่าย D4');
    this.mapValidate.set('glAccountD5', 'กรุณาระบุ บัญชี GL บัญชีจ่าย D5');
    this.mapValidate.set('glCredit', 'กรุณาระบุ บัญชี GL Credit');
    this.mapValidate.set('keyDebit', 'กรุณาระบุ คีย์ผ่านรายการ Debit');
    this.mapValidate.set('keyCredit', 'กรุณาระบุ คีย์ผ่านรายการ Credit');
    this.mapValidate.set('fundSource', 'กรุณาระบุ แหล่งของเงิน');
    this.mapValidate.set('costCenter', 'กรุณาระบุ ศูนย์ต้นทุน');
    this.mapValidate.set('budgetCode', 'กรุณาระบุ รหัสงบประมาณ');
    this.mapValidate.set('fiArea', 'กรุณาระบุ รหัสจังหวัด');
    this.mapValidate.set('mainActivity', 'กรุณาระบุ กิจกรรมหลัก');
    this.mapValidate.set('paymentCenter', 'กรุณาระบุ หน่วยเบิกจ่าย');
  }

  getListGlAccount() {
    const listGl = [
      { accountNo: '1101020203', payType: 'D1' },
      { accountNo: '1101020204', payType: 'D2' },
      { accountNo: '1101020201', payType: 'D3' },
      { accountNo: '1101020205', payType: 'D4' },
      { accountNo: '1101020202', payType: 'D5' },
    ];
    this.listGlAccount = listGl;
    listGl.forEach((gl) => {
      this.generateJuForm.addControl(
        'glAccount' + gl.payType,
        new FormControl('', Validators.required)
      );
      // this.generateJuForm.controls['glAccount' + gl.payType].disable();
    });
  }

  defaultPatchValue() {
    this.generateJuForm.patchValue({
      compCode: '99999',
      documentType: 'JU',
      glCredit: '1101020103',
      keyDebit: '40',
      keyCredit: '50',
      costCenter: '9999999999',
      budgetCode: '99999',
      fiArea: '1000',
      mainActivity: 'P1000',
      paymentCenter: '9999999999',
    });
    this.listGlAccount.forEach((glAccount) => {
      this.generateJuForm.patchValue({
        ['glAccount' + glAccount.payType]: glAccount.accountNo,
      });
    });
    const form = this.generateJuForm.getRawValue();
    if (!this.utils.isEmpty(form.transferDate)) {
      const transferDate: Date = form.transferDate;
      const year = this.utils.CalculateFiscYear(transferDate);
      let fundSource = '';
      if (year.toString().length > 3) {
        fundSource = year.toString().substr(2, 4) + '19000';
      }
      this.generateJuForm.patchValue({
        fundSource: fundSource,
      });
    }
  }

  onChangeDate(type) {
    const form = this.generateJuForm.getRawValue();
    if (type === 'transferDate') {
      this.defaultPatchValue();
    }
  }

  onSearch() {
    this.isSubmitedForm = true;
    const form = this.generateJuForm.getRawValue();
    this.listValidate = [];
    this.validateForm();
    if (this.listValidate.length <= 0) {
      this.isSubmitedForm = false;
      const transferDate: Date = form.transferDate;
      const payload: any = {
        transferDate: this.utils.parseDate(
          transferDate.getDate(),
          transferDate.getMonth() + 1,
          transferDate.getFullYear()
        ),
        paymentDate: this.listPaymentDate,
        paymentName: this.listPaymentName,
        glAccounts: this.listGlAccount,
        testRun: form.isTestGenerate,
        companyCode: form.compCode,
        docType: form.documentType,
        glCredit: form.glCredit,
        keyDebit: form.keyDebit,
        keyCredit: form.keyCredit,
        fundSource: form.fundSource,
        costCenter: form.costCenter,
        budgetCost: form.budgetCode,
        fiArea: form.fiArea,
        mainActivity: form.mainActivity,
        paymentCenter: form.paymentCenter,
        webInfo: this.localStorageService.getWebInfo(),
      };
      this.tempSearchCriteriaExport = payload;
      if (form.isTestGenerate) {
        this.callGenerateJu(payload);
      } else {
        this.openDialogConfirm(payload);
      }
    }
  }

  callGenerateJu(payload) {
    this.generateJuService.generateJu(payload).then((result) => {
      if (result.status === 200) {
        const resultData = [];
        const items = result.data;
        if (items && items.length > 0) {
          items.forEach((items) => {
            this.juHeadId = items.juHeadId;
            resultData.push({
              type: 'HEAD',
              paymentName: items.paymentName,
              paymentDate: items.paymentDate,
              docType: items.docType,
              compCode: items.companyCode,
              dateAcct: items.dateAcct,
            });
            items.list.forEach((list) => {
              list = { ...list, drCr: 'Dr' };
              resultData.push(list);
            });
            resultData.push({
              type: 'SUMMARY',
              amountSum: items.amountCr,
              drCrSum: 'Cr',
              accountNoSum: items.glAccountCr,
            });
          });
        } else {
        }
        this.dataSources = new MatTableDataSource(resultData);
      } else {
      }
      this.isSearch = true;
    });
  }

  openDialogConfirm(payload) {
    const dialogRef = this.dialog.open(DialogConfirmComponent, {
      data: {
        textConfirm: 'คุณต้องการสร้างเอกสารและผ่านรายการ JU ใช่หรือไม่',
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        if (result.event) {
          this.callGenerateJu(payload);
        }
      }
    });
  }

  back() {
    this.isSubmitedForm = false;
    this.isSearch = false;
  }

  validateForm() {
    Object.keys(this.generateJuForm.controls).forEach((key) => {
      const controlErrors = this.generateJuForm.get(key).errors;
      if (controlErrors) {
        this.listValidate.push(this.mapValidate.get(key));
      }
    });
  }

  isHeaderRow(index, item): boolean {
    if (item.type === 'HEAD') {
      return true;
    } else {
      return false;
    }
  }

  isSummaryRow(index, item): boolean {
    if (item.type === 'SUMMARY') {
      return true;
    } else {
      return false;
    }
  }

  openDialogParameterGenerateFile(type): void {
    const dialogRef = this.dialog.open(DialogSearchParameterGenerateFileComponent, {});
    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event) {
        const objValue = result.value;
        this.listPaymentName[0][type] = objValue.generateFileName;
        if (type === 'from') {
          this.generateJuForm.patchValue({
            paymentNameFrom: objValue.generateFileName,
          });
        } else {
          this.generateJuForm.patchValue({
            paymentNameTo: objValue.generateFileName,
          });
        }
      }
    });
  }

  openDialogParameterGenerateFileRange() {
    const changeListPaymentName = [];
    this.listPaymentName.filter((e) => {
      changeListPaymentName.push({
        genFileParameterFrom: e.from,
        genFileParameterTo: e.to,
        optionExclude: e.optionExclude,
      });
    });
    const dialogRef = this.dialog.open(DialogReturnParameterGenFileComponent, {
      data: {
        listGenFileParameter: changeListPaymentName,
        listGenFilePaymentDate: [],
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event) {
        if (result.status === 'save') {
          if (result.value.length > 0) {
            this.listPaymentName = [];
            result.value.forEach((value) => {
              this.listPaymentName.push({
                from: value.genFileParameterFrom,
                to: value.genFileParameterTo,
                optionExclude: value.optionExclude,
              });
            });
            this.generateJuForm.patchValue({
              paymentNameFrom: result.value[0].genFileParameterFrom,
              paymentNameTo: result.value[0].genFileParameterTo,
            });
          }
        }
      }
    });
  }

  patchInputForm(formName, type, typeInput) {
    const form = this.generateJuForm.getRawValue();
    let value = '';
    if (!!form[formName]) {
      if (typeInput === 'DATE') {
        const day = form[formName].getDate();
        const month = form[formName].getMonth() + 1;
        const year = form[formName].getFullYear();
        value = this.utils.parseDate(day, month, year);
      } else {
        value = form[formName];
      }
    }

    if (formName === 'paymentDateFrom' || formName === 'paymentDateTo') {
      this.listPaymentDate[0][type] = value;
    }
  }

  getDetailDocument() {
    const form = this.generateJuForm.getRawValue();
    const transferDate: Date = form.transferDate;
    const payload: any = {
      transferDate: this.utils.parseDate(
        transferDate.getDate(),
        transferDate.getMonth() + 1,
        transferDate.getFullYear()
      ),
      paymentDate: this.listPaymentDate,
      paymentName: this.listPaymentName,
      glAccounts: this.listGlAccount,
      testRun: form.isTestGenerate,
      companyCode: form.compCode,
      docType: form.documentType,
      glCredit: form.glCredit,
      keyDebit: form.keyDebit,
      keyCredit: form.keyCredit,
      fundSource: form.fundSource,
      costCenter: form.costCenter,
      budgetCost: form.budgetCode,
      fiArea: form.fiArea,
      mainActivity: form.mainActivity,
      paymentCenter: form.paymentCenter,
      webInfo: this.localStorageService.getWebInfo(),
    };
    const myObjStr = JSON.stringify(payload);
    const url = this.router.serializeUrl(
      this.router.createUrlTree(['/detail-fi-ju'], {
        queryParams: { searchParams: myObjStr },
      })
    );
    window.open(url, '_blank');
  }

  setPaymentName(type) {
    const form = this.generateJuForm.value;
    if (type === 'paymentNameFrom') {
      if (form.paymentNameFrom) {
        this.listPaymentName[0].from = form.paymentNameFrom;
      } else {
        if (this.listPaymentName.length === 1) {
          this.listPaymentName = [{ from: null, to: null, optionExclude: false }];
        } else {
          this.listPaymentName.splice(0, 1);
          this.generateJuForm.patchValue({
            paymentNameFrom: this.listPaymentName[0].from,
          });
        }
      }
    } else if (type === 'paymentNameTo') {
      if (form.paymentNameTo) {
        this.listPaymentName[0].to = form.paymentNameTo;
      } else {
        this.listPaymentName[0].to = '';
        this.generateJuForm.patchValue({
          paymentNameTo: '',
        });
      }
    }
    // this.checkOptionExclude();
  }

  openDialogVariantCriteria() {
    const dialogRef = this.dialog.open(DialogOmSearchCriteriaComponent, {
      data: {
        role: this.role,
        createBy: this.userProfile.userdata.username,
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event) {
        this.dataCriteria = result.value;
        this.patchToFormByVariantCriteria(result.value.jsonText);
      }
    });
  }

  patchToFormByVariantCriteria(jsonText) {
    const jsonObject = JSON.parse(jsonText);
    this.generateJuForm.patchValue({
      ...jsonObject,
      paymentDateFrom: !!jsonObject.paymentDateFrom ? new Date(jsonObject.paymentDateFrom) : null,
      paymentDateTo: !!jsonObject.paymentDateTo ? new Date(jsonObject.paymentDateTo) : null,
      transferDate: !!jsonObject.transferDate ? new Date(jsonObject.transferDate) : null,
    });
    this.listPaymentName = jsonObject.listPaymentName;
    this.listPaymentDate = jsonObject.listPaymentDate;
  }

  clearSearchCriteria() {
    this.dataCriteria = '';
    this.generateJuForm.reset();
    this.generateJuForm.patchValue({
      transferDate: new Date(),
      isTestGenerate: true,
    });
    this.defaultPatchValue();
    this.getListGlAccount();
  }

  openDialogSaveVariantCriteria(typeAction: string) {
    const searchCriteria = {
      ...this.generateJuForm.value,
      listPaymentName: this.listPaymentName,
      listPaymentDate: this.listPaymentDate,
    };
    const dialogRef = this.dialog.open(DialogOmSaveSearchCriteriaComponent, {
      data: {
        role: this.role,
        createBy: this.userProfile.userdata.username,
        value: searchCriteria,
        type: typeAction,
        dataCriteria: this.dataCriteria,
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event) {
        if (result.status === 'Success') {
          if (result.value) {
            this.dataCriteria = result.value;
          }
        }
      }
    });
  }
}
