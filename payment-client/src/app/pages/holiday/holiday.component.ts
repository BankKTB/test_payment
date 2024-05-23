import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { Holiday } from '@core/models/holiday';
import { SidebarService } from '@core/services';

@Component({
  selector: 'app-holiday',
  templateUrl: './holiday.component.html',
  styleUrls: ['./holiday.component.scss'],
})
export class HolidayComponent implements OnInit {
  constructor(private sidebarService: SidebarService) {}
  ngOnInit() {
    this.sidebarService.updatePageType('config');
    this.sidebarService.updateNowPage('holiday');
  }
}
