import { Component, Input, OnInit } from '@angular/core';
import { LoaderService } from '@core/services';

@Component({
  selector: 'app-loading',
  templateUrl: './loading.component.html',
  styleUrls: ['./loading.component.scss'],
})
export class LoadingComponent implements OnInit {
  @Input() value = 100;
  @Input() diameter = 100;
  @Input() mode = 'indeterminate';
  @Input() strokeWidth = 10;
  @Input() overlay = false;
  @Input() color = 'primary';
  loading: boolean;

  constructor(private loaderService: LoaderService) {
    this.loaderService.loadingVisableChange.subscribe((value) => {
      // console.log(v);
      setTimeout(() => (this.loading = value), 0);
    });
  }

  ngOnInit() {}
}
