import { Component, EventEmitter, Input, OnChanges, ViewChild, Output, OnInit } from '@angular/core';
import { Constant } from '@shared/utils/constant';
import { FormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { Utils } from '@shared/utils/utils';
import { LoaderService, SidebarService } from '@core/services';
import { MatSortable, MatSort } from '@angular/material';
import { MatTableDataSource } from '@angular/material/table';
import { SelectionModel } from '@angular/cdk/collections';
import { MatPaginator } from '@angular/material/paginator';
import { ReturnService } from '@core/services/return/return.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { DialogArrangeColumnComponent } from '@shared/component/dialog-arrange-column/dialog-arrange-column.component';
import { DialogDisplayArrangeColumnComponent } from '@shared/component/dialog-display-arrange-column/dialog-display-arrange-column.component';
import { DialogSaveArrangeColumnComponent } from '@shared/component/dialog-save-arrange-column/dialog-save-arrange-column.component';
import { DialogUpdateArrangeColumnComponent } from '@shared/component/dialog-update-arrange-column/dialog-update-arrange-column.component';
import { DialogSortColumnComponent } from '@shared/component/dialog-sort-column/dialog-sort-column.component';
import { DialogFilterColumnComponent } from '@shared/component/dialog-filter-column/dialog-filter-column.component';
import { DialogReturnLogComponent } from '@shared/component/dialog-return-log/dialog-return-log.component';
import { Sort } from '@core/models/sort';
import { Filter } from '@core/models/filter';
import Decimal from 'decimal.js';
import { ArrangeService } from '@core/services/arrange/arrange.service';
import * as XLSX from 'xlsx';

@Component({
  selector: 'app-table-return-update-status',
  templateUrl: './table-return-update-status.component.html',
  styleUrls: ['./table-return-update-status.component.scss'],
})
export class TableReturnUpdateStatusComponent implements OnChanges, OnInit {
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  @Input() searchCriteria; // receive array list search criteria
  @Input() currentPageType; // receive page name
  @Input() nameComponent; // name of header for multiple header drilldown
  @Output() payBack: EventEmitter<any> = new EventEmitter<any>();
  // @ts-ignore
  @ViewChild(MatPaginator) paginator: MatPaginator;
  totalSumAmount = new Decimal(0);
  displayedColumns: string[] = [
    'id',
    'isComplete',
    'isReturn',
    'fileStatus',
    'refRunning',
    'refLine',
    'paymentDate',
    'paymentName',
    'accountNoFrom',
    'accountNoTo',
    'fileType',
    'fileName',
  ];
  dataSource = new MatTableDataSource([]);
  selectionComplete = new SelectionModel<any>(true, []);
  selectionReturn = new SelectionModel<any>(true, []);
  tempSearchCriteria: any; // store temp for search criteria when back
  listTempDataEdited: any;
  currentPage = 1;
  gap = 5;
  columnsDisplay = [
    {
      key: 'isComplete',
      text: 'สมบูรณ์',
      sequence: 1,
      hide: false,
    },
    {
      key: 'isReturn',
      text: 'คืนกลับ',
      sequence: 2,
      hide: false,
    },
    {
      key: 'accountNoFrom',
      text: 'บ/ช ต้นทาง',
      sequence: 8,
      hide: false,
    },
    {
      key: 'accountNoTo',
      text: 'บ/ช ปลายทาง',
      sequence: 9,
      hide: false,
    },
    {
      key: 'fileName',
      text: 'ชื่อไฟล์',
      sequence: 11,
      hide: false,
    },
    {
      key: 'fileStatus',
      text: 'สถานะ',
      sequence: 3,
      hide: false,
    },
    {
      key: 'fileType',
      text: 'ประเภทไฟล์',
      sequence: 10,
      hide: false,
    },
    {
      key: 'id',
      text: 'id',
      sequence: 12,
      hide: false,
    },
    {
      key: 'paymentDate',
      text: 'วันที่ประมวลผล',
      sequence: 6,
      hide: false,
    },
    {
      key: 'paymentName',
      text: 'การกำหนด',
      sequence: 7,
      hide: false,
    },
    {
      key: 'refLine',
      text: 'บรรทัดอ้าง',
      sequence: 5,
      hide: false,
    },
    {
      key: 'refRunning',
      text: 'Ref Number Index',
      sequence: 4,
      hide: false,
    },
    {
      key: 'transferDate',
      text: 'วันที่สั่ง',
      sequence: 12,
      hide: false,
    },
    {
      key: 'vendor',
      text: 'ผู้ขาย',
      sequence: 13,
      hide: false,
    },
    {
      key: 'bankKey',
      text: 'คีย์ธนาคาร',
      sequence: 14,
      hide: false,
    },
    {
      key: 'vendorBankAccount',
      text: 'เลชที่บัญชีธนาคาร',
      sequence: 15,
      hide: false,
    },
    {
      key: 'transferLevel',
      text: 'ระดับคำสั่ง',
      sequence: 16,
      hide: false,
    },
    {
      key: 'payAccount',
      text: 'บัญชีจ่าย',
      sequence: 17,
      hide: false,
    },
    {
      key: 'paymentCompCode',
      text: 'รหัสหน่วยงาน',
      sequence: 18,
      hide: false,
    },
    {
      key: 'paymentDocument',
      text: 'เลขเอกสาร',
      sequence: 19,
      hide: false,
    },
    {
      key: 'amount',
      text: 'จำนวนเงินใน LC',
      sequence: 20,
      hide: false,
    },
  ];
  multiSort: Sort[] = [
    { id: 'refRunning', direction: 'ASC', sequence: 1 },
    { id: 'refLine', direction: 'DESC', sequence: 2 },
  ];
  filteredColumn: Filter[] = [];
  isSelectedCompleteAll = false;
  isSelectedReturnAll = false;
  arrangeId: number = null;
  defaultArrange: boolean = true;
  fileName = 'step2.xlsx';

  constructor(
    public constant: Constant,
    private formBuilder: FormBuilder,
    private dialog: MatDialog,
    public utils: Utils,
    public loaderService: LoaderService,
    private returnService: ReturnService,
    private snackBar: MatSnackBar,
    private arrangeService: ArrangeService
  ) {}

  ngOnInit() {
    this.defaultArrangeLastRow();
  }

  defaultArrangeLastRow() {
    if (this.defaultArrange) {
      this.arrangeService.findByArrangeCodeLastRow(this.nameComponent).then((response) => {
        if (!this.utils.isEmpty(response.data)) {
          if (response.data.length > 0) {
            this.arrangeId = response.data[0].id ? response.data[0].id : null;
            let arrangeJsonText = null;
            if (!this.utils.isEmpty(response.data[0].arrangeJsonText)) {
              arrangeJsonText = JSON.parse(response.data[0].arrangeJsonText);
            }
            this.columnsDisplay = [...arrangeJsonText];
          }
        }
      });
      this.defaultArrange = false;
    }
  }


  set(name1, name2) {
    return name2;
  }

  ngOnChanges(changes) {
    if (this.currentPageType === this.nameComponent) {
      this.tempSearchCriteria = Object.assign({}, this.searchCriteria);
      this.loadReportHeader();
      this.dataSource.paginator = this.paginator;
      this.clearForm();
    }
  }

  clearForm() {
    // this.listData.forEach((element) => {
    //   this.selectionComplete.deselect(element);
    //   this.selectionReturn.deselect(element);
    // });
  }

  getDisplayedColumns(): string[] {
    return this.columnsDisplay
      .filter((cd) => !cd.hide)
      .sort((a, b) => (a.sequence > b.sequence ? 1 : -1))
      .map((cd) => cd.key);
  }

  loadReportHeader() {
    this.returnService.getReturnPropLogList(this.tempSearchCriteria).then((result) => {
      this.dataSource = new MatTableDataSource();
      if (result.status === 200) {
        this.dataSource = new MatTableDataSource(result.data);
        this.dataSource.paginator = this.paginator;
        this.dataSource.data.forEach((item) => {
          if (item.fileStatus === 'C') {
            // this.selectionComplete.select(item);
          } else if (item.fileStatus === 'R') {
            // this.selectionReturn.select(item);
          } else {
            // this.selectionComplete.deselect(item);
            // this.selectionReturn.deselect(item);
          }
        });
        this.listTempDataEdited = JSON.parse(JSON.stringify(result.data));
      } else if (result.status === 403) {
      }
    });
  }

  sortColumn() {
    this.multiSort.sort((a, b) => (a.sequence > b.sequence ? 1 : -1));
    let sortedData = [];
    this.multiSort.forEach((sort) => {
      sortedData = this.dataSource.data.sort((a, b) => {
        if (sort.direction === 'ASC') {
          return a[sort.id] > b[sort.id] ? 1 : -1;
        } else if (sort.direction === 'DESC') {
          return a[sort.id] > b[sort.id] ? -1 : 1;
        } else {
          return 0;
        }
      });
    });
    this.dataSource.data = sortedData;
  }

  changeFileStatus(index, status, element) {
    const selectedItem = this.dataSource.data.find((e) => e.id === element.id);
    if (status === 'C') {
      this.selectionComplete.select(element);
      this.selectionReturn.deselect(element);
      if (element.fileStatus !== null && status === element.fileStatus) {
        selectedItem.fileStatus = null;
      } else {
        selectedItem.fileStatus = 'C';
      }
      selectedItem.isComplete = true;
      selectedItem.isReturn = false;
    } else {
      this.selectionReturn.select(element);
      this.selectionComplete.deselect(element);
      if (element.fileStatus !== null && status === element.fileStatus) {
        selectedItem.fileStatus = null;
      } else {
        selectedItem.fileStatus = 'R';
      }
      selectedItem.isComplete = false;
      selectedItem.isReturn = true;
    }
  }

  checkStatus(id) {
    const item = this.listTempDataEdited.find((e) => e.id === id);
    if (item.fileStatus === 'C') {
      return 'icon-return-complete';
    } else if (item.fileStatus === 'R') {
      return 'icon-return-return';
    } else {
      return 'icon-return-not-assign';
    }
  }

  checkStatusText(id) {
    const item = this.listTempDataEdited.find((e) => e.id === id);
    if (item.fileStatus === 'C') {
      return 'เรียบร้อยแล้ว';
    } else if (item.fileStatus === 'R') {
      return 'คืนกลับ';
    } else {
      return 'รอการดำเนิน';
    }
  }

  onSubmit() {
    const objRequest = [];
    this.dataSource.data.forEach((item) => {
      if (item.fileStatus !== null) {
        const mapTemp = this.listTempDataEdited.find((e) => e.id === item.id);
        if (item.fileStatus !== mapTemp.fileStatus) {
          objRequest.push({
            id: item.id,
            fileStatus: item.fileStatus,
          });
        }
      }
    });
    this.returnService.saveFileStatusReturn(objRequest).then((result) => {
      if (result && result.status === 200) {
        const payBackObj = {
          type: 'UPDATE_SUCCESS',
          step: '02',
        };
        this.snackBar.open('บันทึกข้อมูลสำเร็จ', '', {
          panelClass: '_success',
          duration: 3000,
        });
        if (result.data.length > 0) {
          const dialogRef = this.dialog.open(DialogReturnLogComponent, {
            width: '95vw',
            data: {
              listLog: result.data,
              logType: 'STATUS',
            },
          });
          dialogRef.afterClosed().subscribe((dialogResult) => {
            if (dialogResult && dialogResult.event) {
              this.loadReportHeader();
              this.payBack.emit(payBackObj);
            }
          });
        }
        // this.payBack.emit(payBackObj);
      }
    });
  }

  onBack() {
    const payBackObj = {
      type: 'BACK',
      step: '02',
    };
    this.payBack.emit(payBackObj);
  }

  openDialogReportFilterColumn() {
    // let columnNotFilter = this.columnTableAll.filter((n1) => {
    //   if (!this.columnCanNotFilter.includes(n1.key)) {
    //     return !this.filterList.some((n2) => {
    //       return n1.key === n2.key;
    //     });
    //   }
    // });
    const dialogRef = this.dialog.open(DialogFilterColumnComponent, {
      width: '90vw',
      data: {
        listAllColumn: this.columnsDisplay,
        filteredColumn: this.filteredColumn,
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event) {
        if (result.status === 'Save') {
          const filteredList = result.value;
          if (filteredList.length > 0) {
            filteredList.forEach((item) => {
              this.filteredColumn.push({
                id: item.id,
                value: item.value,
              });
              this.tempSearchCriteria[item.id] = item.value;
            });
          }
          this.loadReportHeader();
        }
      }
    });
  }

  openDialogReportSortColumn() {
    const dialogRef = this.dialog.open(DialogSortColumnComponent, {
      width: '95vw',
      data: {
        listAllColumn: this.columnsDisplay,
        listColumnSorted: this.multiSort,
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event) {
        if (result.status === 'Save') {
          this.multiSort = result.multiSort;
          this.sortColumn();
        }
      }
    });
  }

  openDialogReportArrangeColumn() {
    this.columnsDisplay = this.columnsDisplay.sort((a, b) => (a.sequence > b.sequence ? 1 : -1));
    const dialogRef = this.dialog.open(DialogArrangeColumnComponent, {
      width: '95vw',
      data: {
        listColumnShow: this.columnsDisplay,
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event) {
        if (result.status === 'Save') {
          this.columnsDisplay = result.columnsDisplay;
          this.columnsDisplay = this.columnsDisplay.sort((a, b) =>
            a.sequence > b.sequence ? 1 : -1
          );
        }
      }
    });
  }

  selectAll(status) {
    if (status === 'C') {
      if (this.isSelectedCompleteAll) {
        this.dataSource.data.forEach((item) => {
          this.selectionComplete.deselect(item);
          const selectedItem = this.dataSource.data.find((e) => e.id === item.id);
          selectedItem.fileStatus = null;
        });
      } else {
        this.dataSource.data.forEach((item) => {
          this.selectionComplete.select(item);
          this.selectionReturn.deselect(item);
          const selectedItem = this.dataSource.data.find((e) => e.id === item.id);
          selectedItem.fileStatus = 'C';
        });
        this.isSelectedReturnAll = false;
      }
      this.isSelectedCompleteAll = !this.isSelectedCompleteAll;
    } else {
      if (this.isSelectedReturnAll) {
        this.dataSource.data.forEach((item) => {
          this.selectionReturn.deselect(item);
          const selectedItem = this.dataSource.data.find((e) => e.id === item.id);
          selectedItem.fileStatus = null;
        });
      } else {
        this.dataSource.data.forEach((item) => {
          this.selectionComplete.deselect(item);
          this.selectionReturn.select(item);
          const selectedItem = this.dataSource.data.find((e) => e.id === item.id);
          selectedItem.fileStatus = 'R';
        });
        this.isSelectedCompleteAll = false;
      }
      this.isSelectedReturnAll = !this.isSelectedReturnAll;
    }
  }
  getTotalSum() {
    return this.dataSource.data.map((t) => t.amount).reduce((acc, value) => acc + value, 0);
  }

  openDialogReportDisplayArrangeColumn() {
    const dialogRef = this.dialog.open(DialogDisplayArrangeColumnComponent, {
      width: '95vw',
      data: {
        listColumnShow: this.columnsDisplay,
        type: this.nameComponent,
        arrangeId: this.arrangeId,
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event) {
        if (result.status === 'success') {
          this.arrangeId = result.id ? result.id : null;
          this.columnsDisplay = [...result.arrangeJsonText];
        }
      }
    });
  }

  openDialogReportSaveArrangeColumn() {
    const dialogRef = this.dialog.open(DialogSaveArrangeColumnComponent, {
      width: '50vw',
      data: {
        listColumnShow: this.columnsDisplay,
        type: this.nameComponent,
        arrangeId: this.arrangeId,
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event) {
        if (result.status === 'success') {
          this.arrangeId = result.data.id;
          this.columnsDisplay = [...result.data.arrangeJsonText];
        }
      }
    });
  }

  openDialogReportUpdateArrangeColumn() {
    const dialogRef = this.dialog.open(DialogUpdateArrangeColumnComponent, {
      width: '50vw',
      data: {
        listColumnShow: this.columnsDisplay,
        type: this.nameComponent,
        arrangeId: this.arrangeId,
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event) {
        if (result.status === 'success') {
          this.arrangeId = result.data.id;
          this.columnsDisplay = [...result.data.arrangeJsonText];
        }
      }
    });
  }

  exportExcel(): void {
    /* table id is passed over here */

    let element = document.getElementById('excel-table');

    console.log(element);

    const ws: XLSX.WorkSheet = XLSX.utils.table_to_sheet(element,  { raw: true });

    for (const row in ws) {
      for (const col in ws[row]) {
        console.log('ws[row][col] ', ws[row][col]);
        if (!isNaN(Number(ws[row][col])) && ws[row][col][0] === '0') {
          ws[row][col] = "'" + ws[row][col];
        }
      }
    }

    // let range = XLSX.utils.decode_range(ws['!ref']);
    // const lengthRow = range.e.r + 1;
    /* hide action column */
    // ws['!ref'] = `A1:E${lengthRow}`;

    /* generate workbook and add the worksheet */
    const wb: XLSX.WorkBook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(wb, ws, 'Sheet1');

    /* save to file */
    XLSX.writeFile(wb, this.fileName);
  }
}
