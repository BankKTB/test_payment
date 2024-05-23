import { Component, ElementRef, OnInit, QueryList, ViewChildren } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DialogConfirmComponent } from '@shared/component/dialog-confirm/dialog-confirm.component';
import { MatDialog } from '@angular/material/dialog';
import { SelectGroupDocumentService } from '@core/services/select-group-document/select-group-document.service';
import { SidebarService } from '@core/services';
import { DialogSearchMasterDataComponent } from '@shared/component/dialog-search-master-data/dialog-search-master-data.component';
import { DialogMultipleSelectDocumentComponent } from '@shared/component/dialog-multiple-select-document/dialog-multiple-select-document.component';
import { Utils } from '@shared/utils/utils';
import { DialogPreviewSelectGroupDocumentComponent } from '@shared/component/dialog-preview-select-group-document/dialog-preview-select-group-document.component';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-select-group-document',
  templateUrl: './select-group-document.component.html',
  styleUrls: ['./select-group-document.component.scss'],
})
export class SelectGroupDocumentComponent implements OnInit {
  @ViewChildren('companyCode') companyCode: QueryList<ElementRef>;
  @ViewChildren('from') from: QueryList<ElementRef>;
  @ViewChildren('to') to: QueryList<ElementRef>;
  @ViewChildren('optionExclude') optionExclude: QueryList<ElementRef>;

  checkCompCode = false;
  checkDocument = false;
  isSubmitedForm = false;
  isSubmitFormForValidateGroupList = false;
  selectForm: FormGroup;
  listValidate = [];
  groupDocumentList = [
    {
      companyCode: null,
      list: [
        { from: null, to: null, optionExclude: false },
        { from: null, to: null, optionExclude: false },
        { from: null, to: null, optionExclude: false },
      ],
    },
  ];

  optionExcludeDocumentNo = 'N';

  constructor(
    private formBuilder: FormBuilder,
    private dialog: MatDialog,
    private selectGroupDocumentService: SelectGroupDocumentService,
    private sidebarService: SidebarService,
    private utils: Utils,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit() {
    this.sidebarService.updatePageType('payment');
    this.sidebarService.updateNowPage('selectGroupDocument');

    this.createFormGroup();
  }

  createFormGroup() {
    this.utils.fiscYear = this.utils.CalculateFiscYear(new Date());

    this.selectForm = this.formBuilder.group({
      fiscalYear: this.formBuilder.control(this.utils.fiscYear, [Validators.required]),
      defineName: this.formBuilder.control('', [Validators.required]),
    });
  }

  setData(index) {
    const companyCode = this.companyCode.toArray()[index].nativeElement.value;
    const from = this.from.toArray()[index].nativeElement.value;
    const to = this.to.toArray()[index].nativeElement.value;

    if (companyCode) {
      this.groupDocumentList[index].companyCode = companyCode;
    } else {
      this.groupDocumentList[index].companyCode = '';
    }
    if (from) {
      this.groupDocumentList[index].list[0].from = from;
    } else {
      this.groupDocumentList[index].list[0].from = '';
    }
    if (to) {
      this.groupDocumentList[index].list[0].to = to;
    } else {
      this.groupDocumentList[index].list[0].to = '';
    }
  }

  setOptionInclude(index) {
    const valueIndex = this.optionExclude.toArray()[index] as any;

    const optionExclude = valueIndex._checked;
    if (optionExclude) {
      this.groupDocumentList[index].list[0].optionExclude = optionExclude;
    } else {
      this.groupDocumentList[index].list[0].optionExclude = false;
    }
  }

  addInputVendor() {
    this.isSubmitFormForValidateGroupList = false;
    this.groupDocumentList.push({
      companyCode: null,
      list: [
        { from: null, to: null, optionExclude: false },
        { from: null, to: null, optionExclude: false },
        { from: null, to: null, optionExclude: false },
      ],
    });
  }

  deleteInputVendor(index) {
    if (this.groupDocumentList.length > 1) {
      this.groupDocumentList.splice(index, 1);
    }
  }

  onSave() {
    const form = this.selectForm.getRawValue();
    const object = {
      groupList: this.groupDocumentList,
    };
    form.jsonText = JSON.stringify(object);
    console.log(form);

    const dialogRef = this.dialog.open(DialogConfirmComponent, {
      data: {
        textConfirm: 'ยืนยันการสร้าง ',
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      console.log(result);
      if (result) {
        if (result.event) {
          this.createSave(form);
        }
      }
    });
  }

  createSave(payload) {
    this.selectGroupDocumentService.create(payload).then((result) => {
      console.log(result);
    });
  }

  validateGroupDocumentList() {
    const groupDocumentList = [...this.groupDocumentList];
    return groupDocumentList.filter((groupDocument) => {
      return (
        groupDocument.companyCode === null ||
        groupDocument.companyCode === '' ||
        groupDocument.list[0].from === null ||
        groupDocument.list[0].from === ''
      );
    });
  }

  isErrorGroupDocumentInputDocument(groupDocument) {
    if (this.isSubmitFormForValidateGroupList) {
      return !groupDocument.list.some((e) => e.from);
    }
    return false;
  }

  isErrorGroupDocumentInputCompCode(groupDocument, index) {
    if (this.isSubmitFormForValidateGroupList) {
      return !!!groupDocument.companyCode;
    }
    return false;
  }

  onPreview() {
    const form = this.selectForm.getRawValue();
    this.isSubmitedForm = true;
    this.isSubmitFormForValidateGroupList = true;
    this.listValidate = [];

    if (!form.fiscalYear) {
      this.listValidate.push('กรุณาระบุ ปีบัญชี');
    }
    if (!form.defineName) {
      this.listValidate.push('กรุณาระบุ การกำหนด');
    }

    let checkData = false;
    for (const item of this.groupDocumentList) {
      if (item.companyCode) {
        for (const object of item.list) {
          // console.log(object);
          if (object.from) {
            checkData = false;
            break;
          } else {
            this.checkDocument = true;
            checkData = true;
          }
        }
        break;
      } else {
        this.checkCompCode = true;
        checkData = true;
      }
    }
    if (checkData || this.validateGroupDocumentList().length > 0) {
      this.listValidate.push('กรุณาระบุ รหัสหน่วยงาน และ เลขที่เอกสาร');
    }

    if (this.listValidate.length <= 0) {
      this.isSubmitedForm = false;
      this.isSubmitFormForValidateGroupList = false;
      const object = {
        groupList: this.groupDocumentList,
      };
      form.jsonText = JSON.stringify(object);
      this.selectGroupDocumentService.preview(form).then((response) => {
        console.log(response);
        if (response.status === 200) {
          const dialogRef = this.dialog.open(DialogPreviewSelectGroupDocumentComponent, {
            data: {
              object: response.data,
              request: form,
            },
          });
          dialogRef.afterClosed().subscribe((result) => {
            console.log(result);
            if (result) {
              if (result.event) {
              }
            }
          });
        } else if (response.status === 404) {
          this.snackBar.open('ไม่มีเงื่อนข้อมูลเงื่อนไขที่ระบุ', '', {
            panelClass: '_warning',
          });
        }
      });
    }
  }

  openDialogOmSearchCriteria() {}

  clearInput() {
    this.selectForm.patchValue({
      fiscalYear: '',
      defineName: '',
    });
    this.groupDocumentList = [
      {
        companyCode: null,
        list: [
          { from: null, to: null, optionExclude: false },
          { from: null, to: null, optionExclude: false },
          { from: null, to: null, optionExclude: false },
        ],
      },
      {
        companyCode: null,
        list: [
          { from: null, to: null, optionExclude: false },
          { from: null, to: null, optionExclude: false },
          { from: null, to: null, optionExclude: false },
        ],
      },
      {
        companyCode: null,
        list: [
          { from: null, to: null, optionExclude: false },
          { from: null, to: null, optionExclude: false },
          { from: null, to: null, optionExclude: false },
        ],
      },
    ];
  }

  openDialogSearchCompanyCode(index, type): void {
    const dialog = this.dialog.open(DialogSearchMasterDataComponent, {
      data: { type },
    });
    dialog.afterClosed().subscribe((result) => {
      if (result && result.event) {
        if (type === 'companyCode') {
          this.groupDocumentList[index].companyCode = result.value;
        }
      }
    });
  }

  openDialogMultipleSelectDocument(index) {
    const dialogRef = this.dialog.open(DialogMultipleSelectDocumentComponent, {
      data: {
        list: this.groupDocumentList[index].list,
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log(result);
      if (result && result.event) {
        if (result.status === 'save') {
          this.groupDocumentList[index].list = result.value;
        }
      }
    });
  }

  copyFromExcel(event, index, type, digit) {
    console.log(event);

    const value = event.data;

    console.log('value' + value);

    if (value && value.length >= digit) {
      // console.log('index' + index);

      console.log(value.split('\n'));
      const list = value.split('\n');

      if (list.length > this.groupDocumentList.length) {
        const need = list.length - this.groupDocumentList.length;
        for (let i = 0; i < need; i++) {
          this.groupDocumentList.push({
            companyCode: null,
            list: [
              { from: null, to: null, optionExclude: false },
              { from: null, to: null, optionExclude: false },
              { from: null, to: null, optionExclude: false },
            ],
          });
        }
      }
      console.log(this.groupDocumentList);
      if (type === 'companyCode') {
        for (let i = index; i < list.length; i++) {
          this.groupDocumentList[i].companyCode = list[i];
        }
      } else if (type === 'from') {
        for (let i = index; i < list.length; i++) {
          this.groupDocumentList[i].list[i].from = list[i];
        }
      } else if (type === 'to') {
        for (let i = index; i < list.length; i++) {
          this.groupDocumentList[i].list[i].to = list[i];
        }
      }
    }
  }
}
