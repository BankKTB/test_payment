import { Component, OnInit, ViewChild, Inject } from '@angular/core';
import { MatSort } from '@angular/material/sort';
import { SelectionModel } from '@angular/cdk/collections';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ArrangeReques } from '@core/models/arrange/arrange-reques';
import { Utils } from '@shared/utils/utils';
import { ArrangeService } from '@core/services/arrange/arrange.service';
import { ArrangeResponse } from '@core/models/arrange/arrange-response';

export interface DialogData {
  listColumnShow: any;
  type: '';
  arrangeId: null;
}

@Component({
  selector: 'app-dialog-arrange-column',
  templateUrl: './dialog-save-arrange-column.component.html',
  styleUrls: ['./dialog-save-arrange-column.component.scss'],
})
export class DialogSaveArrangeColumnComponent implements OnInit {
  arrangeForm: FormGroup;
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  isSubmitedForm = false;
  listValidate = [];

  constructor(
    private dialogRef: MatDialogRef<DialogSaveArrangeColumnComponent>,
    private snackBar: MatSnackBar,
    private formBuilder: FormBuilder,
    private utils: Utils,
    private arrangeService: ArrangeService,
    @Inject(MAT_DIALOG_DATA) public data: DialogData
  ) {
    this.createFormGroup();
  }

  ngOnInit() {}

  createFormGroup() {
    this.arrangeForm = this.formBuilder.group({
      arrangeName: this.formBuilder.control('', [Validators.required]),
      arrangeDescription: this.formBuilder.control(''),
    });
  }

  clickSave() {
    const form = this.arrangeForm.value;
    this.listValidate = [];
    if (!form.arrangeName) {
      this.listValidate.push('กรุณาระบุชื่อโครงร่าง');
    }
    this.isSubmitedForm = true;
    if (this.listValidate.length <= 0) {
      this.isSubmitedForm = false;
      const body: ArrangeReques = {
        id: null,
        arrangeCode: this.data.type,
        arrangeName: form.arrangeName,
        arrangeDescription: form.arrangeDescription,
        arrangeJsonText: JSON.stringify(this.data.listColumnShow),
      };
      this.save(body);
    }
  }

  update(body: ArrangeReques) {
    this.arrangeService.update(body, this.data.arrangeId).then((response) => {
      if (response.status === 201) {
        this.snackBar.open('บันทึกข้อมูลสำเร็จ', 'close', {
          duration: 7000,
          panelClass: ['success-snackbar'],
        });
        let arrangeJsonText = null;
        if (!this.utils.isEmpty(response.data.arrangeJsonText)) {
          arrangeJsonText = JSON.parse(response.data.arrangeJsonText);
        }
        let obj: ArrangeResponse = {
          id: response.data.id,
          arrangeCode: response.data.arrangeCode,
          arrangeName: response.data.arrangeName,
          arrangeDescription: response.data.arrangeDescription,
          arrangeJsonText: arrangeJsonText,
        };
        this.dialogRef.close({
          event: true,
          status: 'success',
          data: obj,
        });
      } else {
      }
    });
  }

  save(body: ArrangeReques) {
    this.arrangeService.save(body).then((response) => {
      if (response.status === 201) {
        this.snackBar.open('บันทึกข้อมูลสำเร็จ', 'close', {
          duration: 7000,
          panelClass: ['success-snackbar'],
        });
        let arrangeJsonText = null;
        if (!this.utils.isEmpty(response.data.arrangeJsonText)) {
          arrangeJsonText = JSON.parse(response.data.arrangeJsonText);
        }
        let obj: ArrangeResponse = {
          id: response.data.id,
          arrangeCode: response.data.arrangeCode,
          arrangeName: response.data.arrangeName,
          arrangeDescription: response.data.arrangeDescription,
          arrangeJsonText: arrangeJsonText,
        };
        this.dialogRef.close({
          event: true,
          status: 'success',
          data: obj,
        });
      } else if (response.status === 403) {
        this.snackBar.open('ชื่อโครงร่างซ้ำ กรุณาใช้ชื่ออื่น', 'close', {
          duration: 5000,
          panelClass: ['error-snackbar'],
        });
      } else {
        this.snackBar.open('เกิดข้อผิดพลาดบางประการ กรุณาติดต่อผู้ดูแลระบบ', 'close', {
          duration: 5000,
          panelClass: ['error-snackbar'],
        });
      }
    });
  }

  closeDialog() {
    this.dialogRef.close({
      event: true,
      status: 'Close',
      value: '',
    });
  }
}
