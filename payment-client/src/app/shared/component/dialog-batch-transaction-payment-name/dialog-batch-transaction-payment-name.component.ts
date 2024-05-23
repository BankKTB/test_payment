import { AfterViewInit, Component, Inject, OnInit, ViewChild } from '@angular/core';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { ThaidatePipe } from '@shared/pipe/thaidate.pipe';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { GenerateFileService } from '@core/services/generate-file/generate-file.service';
import { BatchJobService } from '@core/services/batch-job/batch-job.service';

export interface DialogData {
  title: string;
}

@Component({
  selector: 'app-dialog-batch-transaction-payment-name',
  templateUrl: './dialog-batch-transaction-payment-name.component.html',
  styleUrls: ['./dialog-batch-transaction-payment-name.component.scss'],
})
export class DialogBatchTransactionPaymentNameComponent implements OnInit, AfterViewInit {
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  displayedColumns: string[] = ['choose', 'generateFileDate', 'generateFileName', 'statusName'];

  dataSource = new MatTableDataSource([]);

  pipe: ThaidatePipe;
  errorMessage = '';
  title: string = 'ข้อจำกัด';

  constructor(
    private dialogRef: MatDialogRef<DialogBatchTransactionPaymentNameComponent>,
    private generateFileService: GenerateFileService,
    private batchJobService: BatchJobService,
    @Inject(MAT_DIALOG_DATA) public data: DialogData
  ) {
    if (this.data && this.data.title) {
      this.title = this.data.title;
    }
  }

  closeDialog(): void {
    const dialogRef = this.dialogRef.close();
  }

  ngOnInit() {}

  ngAfterViewInit(): void {
    this.searchAllParameter();
  }

  chooseDataSearch(data) {
    console.log(data);
    // this.errorMessage = '';
    this.dialogRef.close({
      event: true,
      // type: this.data.type,
      value: data,
    });
  }

  searchAllParameter() {
    this.batchJobService.findCreateJobByCondition('**').then((result) => {
      console.log(result);
      if (result.status === 200) {
        const data = result.data;
        if (data) {
          this.dataSource = new MatTableDataSource(data);
          this.dataSource.sort = this.sort;
          this.setDateThaiForFilter();
        }
      } else if (result.status === 404) {
        this.dataSource = new MatTableDataSource([]);
      }
    });
  }

  searchByValue(event) {
    const textSearch = event.value;
    console.log(textSearch);
    if (textSearch !== '') {
      this.batchJobService
        .findCreateJobByCondition(this.checkStarAndPercent(textSearch))
        .then((result) => {
          console.log('searchByValue : ', result);
          if (result.status === 200) {
            const data = result.data;
            if (data) {
              this.dataSource = new MatTableDataSource(data);
              this.dataSource.sort = this.sort;
              this.setDateThaiForFilter();
            }
          } else if (result.status === 404) {
            this.dataSource = new MatTableDataSource([]);
          }
        });
    } else {
      this.searchAllParameter();
    }
  }

  applyFilter(filterValue: string) {
    console.log(filterValue);
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  setDateThaiForFilter() {
    this.pipe = new ThaidatePipe();
    // console.log('hi', this.dataSource.filterPredicate);
    const defaultPredicate = this.dataSource.filterPredicate;
    this.dataSource.filterPredicate = (data, filter) => {
      const formatted = this.pipe.transform(data.paymentDate, '');
      return formatted.indexOf(filter) >= 0 || defaultPredicate(data, filter);
    };
  }

  checkStarAndPercent(textSearch) {
    console.log(textSearch);
    const slash = textSearch.split('').filter((value) => value === '/').length;
    if (slash > 0) {
      for (let i = 0; i < slash; i++) {
        textSearch = textSearch.replace('/', '');
      }
    }
    console.log(textSearch);

    const percent = textSearch.split('').filter((value) => value === '%').length;
    if (percent > 0) {
      for (let i = 0; i < percent; i++) {
        textSearch = textSearch.replace('%', '*');
      }
      const textStar = textSearch.split('').filter((value) => value === '*').length;
      if (textStar > 2) {
        this.errorMessage = 'เงื่อนไขไม่ถูกต้อง';
        return 'error';
      } else {
        const checkCondtion = textSearch.indexOf('**');
        if (checkCondtion === -1) {
          if (textSearch === '*' || textSearch === '**') {
            this.errorMessage = 'เงื่อนไขไม่ถูกต้อง';
            return 'error';
          }
        } else {
          this.errorMessage = 'เงื่อนไขไม่ถูกต้อง';
          return 'error';
        }
      }
    } else {
      const textStar = textSearch.split('').filter((value) => value === '*').length;
      if (textStar === 0) {
        textSearch = '*' + textSearch + '*';
      }
    }
    return textSearch;
  }
}
