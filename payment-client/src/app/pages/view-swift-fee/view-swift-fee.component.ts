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
import { SwiftFeeService } from '@core/services/swift-fee/swift-fee.service';
import { DialogEditSwiftFeeComponent } from '@shared/component/dialog-edit-swift-fee/dialog-edit-swift-fee.component';
import { DialogConfirmDeleteMasterComponent } from '@shared/component/dialog-confirm-delete-master/dialog-confirm-delete-master.component';
import { DialogSaveSwiftFeeComponent } from '@shared/component/dialog-save-swift-fee/dialog-save-swift-fee.component';
import * as XLSX from 'xlsx';

@Component({
  selector: 'app-view-swift-fee',
  templateUrl: './view-swift-fee.component.html',
  styleUrls: ['./view-swift-fee.component.scss'],
})
export class ViewSwiftFeeComponent implements OnInit {
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  displayedColumns: string[] = [
    'bankKey',
    'baseAmount',
    'baseFee',
    'varAmount',
    'varFee',
    'maxFee',
    'smart',
  ];

  dataSource = new MatTableDataSource([]);
  fileName = 'swiftFee.xlsx';

  constructor(
    public constant: Constant,
    private dialog: MatDialog,
    private utils: Utils,
    private router: Router,
    private formBuilder: FormBuilder,
    private companyPayingService: CompanyPayingService,
    private snackBar: MatSnackBar,
    private sidebarService: SidebarService,
    private swiftFeeService: SwiftFeeService
  ) {}

  ngOnInit() {
    this.sidebarService.updatePageType('view');
    this.sidebarService.updateNowPage('view-swiftFee');

    this.searchAll();
  }

  searchAll() {
    this.dataSource = new MatTableDataSource([]);
    this.swiftFeeService.searchAll().then((result) => {
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

  openDialogEditSwiftFee(item) {
    const dialogRef = this.dialog.open(DialogEditSwiftFeeComponent, {
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

  deleteInput(element) {
    const dialogRef = this.dialog.open(DialogConfirmDeleteMasterComponent, {
      data: { value: null },
      disableClose: true,
    });
    dialogRef.afterClosed().subscribe((response) => {
      if (response) {
        if (response.event && response.value === 'Delete') {
          const id = element.id;
          this.swiftFeeService.delete(id).then((result) => {
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

  openDialogSaveSwiftFee() {
    const dialogRef = this.dialog.open(DialogSaveSwiftFeeComponent, {
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
    const dialogRef = this.dialog.open(DialogSaveSwiftFeeComponent, {
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

  exportExcel() {
    /* table id is passed over here */

    let element = document.getElementById('excel-table');

    const ws: XLSX.WorkSheet = XLSX.utils.table_to_sheet(element);

    /* delete action column */
    let range = XLSX.utils.decode_range(ws['!ref']);
    const lengthRow = range.e.r + 1;
    /* hide action column */
    ws['!ref'] = `A1:G${lengthRow}`;

    /* generate workbook and add the worksheet */
    const wb: XLSX.WorkBook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(wb, ws, 'Sheet1');

    /* save to file */
    XLSX.writeFile(wb, this.fileName);
  }
}
