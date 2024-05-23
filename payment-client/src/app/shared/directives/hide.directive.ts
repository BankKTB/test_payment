import { Directive, ElementRef, Input, OnInit } from '@angular/core';
import { AuthorizeService } from '@core/services';

@Directive({
  selector: '[hideIfUnauthorize]',
})
export class HideDirective implements OnInit {
  @Input('hideIfUnauthorize') permission;

  constructor(private el: ElementRef, private authorizeService: AuthorizeService) {}

  ngOnInit() {
    window.scrollTo(0, 0);
    if (!this.authorizeService.hasPermission(this.permission)) {
      this.el.nativeElement.style.display = 'none';
    }
  }
}
