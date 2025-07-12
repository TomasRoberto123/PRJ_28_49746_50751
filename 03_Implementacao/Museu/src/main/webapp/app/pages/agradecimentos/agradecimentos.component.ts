import { Component, HostListener, OnInit } from '@angular/core';
import { ViewportScroller } from '@angular/common';

@Component({
  selector: 'jhi-agradecimentos',
  templateUrl: './agradecimentos.component.html',
  styleUrls: ['./agradecimentos.component.scss'],
})
export class AgradecimentosComponent implements OnInit {
  pageYoffset = 0;
  constructor(private scroll: ViewportScroller) {
    // empty
  }
  @HostListener('window:scroll', ['$event']) onScroll() {
    this.pageYoffset = window.pageYOffset;
  }

  scrollToTop() {
    this.scroll.scrollToPosition([0, 0]);
  }
  ngOnInit(): void {
    // empty
  }

}
