import { Component, OnInit } from '@angular/core';

declare let require: any;

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss'],
})
export class FooterComponent implements OnInit {
  // appVersion = process.env.npm_package_version;
  appVersion = require('../../../../../package.json').version;
  isLoading = false;

  constructor() {}

  ngOnInit() {}
}
