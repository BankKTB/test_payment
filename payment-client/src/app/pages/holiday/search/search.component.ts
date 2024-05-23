import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { Holiday } from '@core/models/holiday';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HolidayService } from '@core/services/holiday/holiday.service';
import { HolidayRequest } from '@core/models/holiday-request';
import { Utils } from '@shared/utils/utils';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatPaginator } from '@angular/material/paginator';
import { SidebarService } from '@core/services';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss'],
})
export class SearchComponent implements OnInit {
  displayedColumnsCreate: string[] = ['holidayDate', 'description', 'active', 'action'];
  displayedColumnsSearch: string[] = ['holidayDate', 'description', 'active'];
  dataSource = new MatTableDataSource<Holiday>();
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  isOpenManageForm: boolean = false;
  myForm: FormGroup;
  myFormSearch: FormGroup;
  isEdit: boolean = false;
  isMethodEdit: boolean = false;
  constructor(
    private formBuilder: FormBuilder,
    private holidayService: HolidayService,
    private snackBar: MatSnackBar,
    private utils: Utils,
    private activatedRoute: ActivatedRoute,
    private sidebarService: SidebarService
  ) {
    this.myForm = this.formBuilder.group({
      holidayDate: this.formBuilder.control('', [Validators.required]),
      description: this.formBuilder.control('', [Validators.required]),
      isActive: this.formBuilder.control(true),
    });

    this.myFormSearch = this.formBuilder.group({
      fiscalYear: this.formBuilder.control(''),
      searchDate: this.formBuilder.control(''),
    });
  }

  ngOnInit() {
    this.loadHoliday();
    this.sidebarService.updatePageType('config');
    this.sidebarService.updateNowPage('holiday');
    this.isMethodEdit = this.activatedRoute.url['value'][0].path === 'create';
  }

  setDisplayColumn(): string[] {
    return this.isMethodEdit ? this.displayedColumnsCreate : this.displayedColumnsSearch;
  }

  onSearch() {}

  loadHoliday() {
    const myFormSearch = this.myFormSearch.getRawValue();
    let date = '';
    let fiscalYear = '';
    if (myFormSearch.fiscalYear) {
      fiscalYear = (Number(myFormSearch.fiscalYear) - 543).toString();
    }
    if (myFormSearch.searchDate) {
      date = this.utils.parseDate(
        myFormSearch.searchDate.getDate(),
        myFormSearch.searchDate.getMonth() + 1,
        myFormSearch.searchDate.getFullYear()
      );
    }
    this.holidayService.searchAll(fiscalYear, date).then((res: any) => {
      if (res && res.status === 200) {
        this.dataSource = new MatTableDataSource<Holiday>(res.data.holidays);
        this.dataSource.paginator = this.paginator;
      } else {
        this.openSnackBar('ไม่สามารถเรียกข้อมูลได้', '', '_warning');
      }
    });
  }

  onEdit(item: Holiday) {
    this.isOpenManageForm = true;
    this.myForm.patchValue({
      description: item.description,
      holidayDate: new Date(item.date),
    });
    this.isEdit = true;
  }

  onSubmitManage() {
    this.isOpenManageForm = false;
    const dateSelected: Date = this.myForm.controls.holidayDate.value;
    const request: HolidayRequest = {
      holidays: [
        {
          active: this.myForm.controls.isActive.value,
          date: this.utils.parseDate(
            dateSelected.getDate(),
            dateSelected.getMonth() + 1,
            dateSelected.getFullYear()
          ),
          description: this.myForm.controls.description.value,
        },
      ],
    };
    if (this.isEdit) {
      // Update
      this.holidayService.update(request).then((res) => {
        if (res.status === 200) {
          this.openSnackBar('บันทึกข้อมูลสำเร็จ', '', '_success');
          this.loadHoliday();
        } else {
          let textError = 'ไม่สามารถบันทึกได้';
          if (res.error.data.errors.length > 1) {
            textError = res.error.data.errors[1].text;
          }
          this.openSnackBar(textError, '', '_warning');
        }
      });
    } else {
      // Create
      this.holidayService.create(request).then((res) => {
        if (res.status === 200) {
          this.openSnackBar('บันทึกข้อมูลสำเร็จ', '', '_success');
          this.loadHoliday();
        } else {
          let textError = 'ไม่สามารถบันทึกได้';
          if (res.error.data.errors.length > 1) {
            textError = res.error.data.errors[1].text;
          }
          this.openSnackBar(textError, '', '_warning');
        }
      });
    }
  }

  onClearUpdate() {
    this.isEdit = false;
    this.isOpenManageForm = false;
    this.myForm.reset();
    this.myForm.patchValue({
      isActive: true,
    });
  }

  openSnackBar(message?: string, action?: string, panelClass?: string) {
    this.snackBar.open(message, action, {
      panelClass,
    });
  }

  clearSearch() {
    this.myFormSearch.reset();
    this.loadHoliday();
  }
}
