import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { MatSort } from '@angular/material/sort';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Constant } from '@shared/utils/constant';
import { Utils } from '@shared/utils/utils';



@Component({
  selector: 'app-dialog-om-input-reason-unapproved',
  templateUrl: './dialog-om-input-reason-unapproved.component.html',
  styleUrls: ['./dialog-om-input-reason-unapproved.component.scss'],
})
export class DialogOmInputReasonUnapprovedComponent implements OnInit {

  @ViewChild(MatSort, { static: true }) sort: MatSort;
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  dataSource = new MatTableDataSource([]);
  header = null;

  isConfirm = false;
  reason = '';


  constructor(
    public dialogRef: MatDialogRef<DialogOmInputReasonUnapprovedComponent>,
    public constant: Constant,
    public utils: Utils,
  ) {
    this.dialogRef.disableClose = false;

  }

  ngOnInit() {
  }

  setReasonTextArea(event) {
    console.log(event)
    this.reason = event.target.value;

  }

  clickConfirm(): void {
    this.dialogRef.close({ event: 'save', value: this.reason });
  }

  clickCancel(): void {
    this.dialogRef.close({ event: 'close' });
  }


  // onChangeReason(event) {
  //   const object = JSON.parse(this.data.paymentAlias.jsonText);
  //   const parameter = object.parameter;
  //   if (event === '06' || event === '07') {
  //     this.reverseForm.controls.reverseDate.enable();
  //   } else {
  //     if (this.data.paymentAlias) {
  //       this.reverseForm.controls.reverseDate.disable();
  //       this.reverseForm.patchValue({
  //         reverseDate: new Date(parameter.postDate ? new Date(parameter.postDate) : ''),
  //       });
  //     } else {
  //       this.reverseForm.patchValue({
  //         reverseDate: new Date(),
  //       });
  //     }
  //   }
  // }
}
