import { Component, OnInit, ViewChild } from '@angular/core';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Constant } from '@shared/utils/constant';
import { MatDialog } from '@angular/material/dialog';
import { Utils } from '@shared/utils/utils';
import { Router } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { CompanyPayingService } from '@core/services/company-paying/company-paying.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SidebarService } from '@core/services';
import { SumFileConditionService } from '@core/services/sum-file-condition/sum-file-condition.service';
import { DialogEditSumFileConditionComponent } from '@shared/component/dialog-edit-sum-file-condition/dialog-edit-sum-file-condition.component';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { APP_DATE_FORMATS, AppDateAdapter } from '@shared/utils/format-datepicker';
import { DialogSaveSumFileConditionComponent } from '@shared/component/dialog-save-sum-file-condition/dialog-save-sum-file-condition.component';
import * as XLSX from 'xlsx';
import { DialogConfirmDeleteMasterComponent } from '@shared/component/dialog-confirm-delete-master/dialog-confirm-delete-master.component';
import { DialogConfirmStatusUsabilityComponent } from '@shared/component/dialog-confirm-status-usability/dialog-confirm-status-usability.component';

@Component({
  selector: 'app-sum-file-condition',
  templateUrl: './sum-file-condition.component.html',
  styleUrls: ['./sum-file-condition.component.scss'],
})
export class SumFileConditionComponent implements OnInit {
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  displayedColumns: string[] = [
    'paymentMethod',
    'vendorFrom',
    'vendorTo',

    'validFrom',
    'validTo',

    'createdBy',
    'created',
    'timeCreate',
    'updatedBy',
    'updated',
    'timeUpdate',
    'active',
    'edit',
  ];

  dataSource = new MatTableDataSource([]);

  fileName = 'sumFileCondition.xlsx';

  constructor(
    public constant: Constant,
    private dialog: MatDialog,
    private utils: Utils,
    private router: Router,
    private formBuilder: FormBuilder,
    private companyPayingService: CompanyPayingService,
    private snackBar: MatSnackBar,
    private sidebarService: SidebarService,
    private sumFileConditionService: SumFileConditionService
  ) {}

  ngOnInit() {
    this.sidebarService.updatePageType('config');
    this.sidebarService.updateNowPage('sumFileCondition');

    this.searchAll();
  }

  searchAll() {
    this.dataSource = new MatTableDataSource([]);
    this.sumFileConditionService.searchAll().then((result) => {
      console.log(result);
      if (result.status === 200) {
        const data = result.data;
        if (data) {
          this.dataSource = new MatTableDataSource(data);

          this.dataSource.sort = this.sort;
        }
      } else if (result.status === 404) {
        this.dataSource = new MatTableDataSource([]);
      }
    });
  }

  deleteInputCompanyCode(element) {
    const id = element.id;
    this.companyPayingService.delete(id).then((result) => {
      console.log(result);
      if (result.status === 201) {
        const data = result.data;
        if (data) {
          this.searchAll();
        }
      } else if (result.status === 404) {
      }
    });
  }

  openDialogEditSumFileCondition(item) {
    const dialogRef = this.dialog.open(DialogEditSumFileConditionComponent, {
      width: '65vw',
      data: {
        item,
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        if (result.event && result.saveSuccess) {
          this.searchAll();
        }
      }
    });
  }

  openDialogSaveSumFileCondition() {
    const dialogRef = this.dialog.open(DialogSaveSumFileConditionComponent, {
      width: '65vw',
      data: { copyValue: null },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        if (result.event && result.saveSuccess) {
          this.searchAll();
        }
      }
    });
  }

  copy(data) {
    const dialogRef = this.dialog.open(DialogSaveSumFileConditionComponent, {
      width: '65vw',
      data: { copyValue: data },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        if (result.event && result.saveSuccess) {
          this.searchAll();
        }
      }
    });
  }

  deleteInput(element) {
    console.log(element);

    const dialogRef = this.dialog.open(DialogConfirmStatusUsabilityComponent, {
      data: {
        statusActive: element.active,
      },
      disableClose: true,
    });
    dialogRef.afterClosed().subscribe((response) => {
      if (response) {
        if (response.event && response.value === 'Delete') {
          const id = element.id;
          this.sumFileConditionService.delete(id).then((result) => {
            console.log(result);
            if (result.status === 201) {
              const data = result.data;
              if (data) {
                this.searchAll();
              }
            } else if (result.status === 404) {
            }
          });
        }
      }
    });
  }

  exportExcel(): void {
    /* table id is passed over here */

    let element = document.getElementById('excel-table');

    console.log(element);

    const ws: XLSX.WorkSheet = XLSX.utils.table_to_sheet(element);

    /* hide action column */
    let range = XLSX.utils.decode_range(ws['!ref']);
    const lengthRow = range.e.r + 1;
    ws['!ref'] = `A1:H${lengthRow}`;
    /* generate workbook and add the worksheet */
    const wb: XLSX.WorkBook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(wb, ws, 'Sheet1');

    /* save to file */
    XLSX.writeFile(wb, this.fileName);
  }
}
