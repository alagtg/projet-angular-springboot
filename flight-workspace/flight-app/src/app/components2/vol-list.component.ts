import { Component, Input, OnChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Vol } from '../services/vol.service';

@Component({
  selector: 'app-vol-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './vol-list.component.html',
  styleUrls: ['./vol-list.component.scss']
})
export class VolListComponent implements OnChanges {
  @Input() vols: Vol[] = [];

  pagedVols: Vol[] = [];
  currentPage = 1;
  pageSize = 5; // nombre d'éléments par page
  totalPages = 1;

  ngOnChanges() {
    this.updatePagination();
  }

  updatePagination() {
    if (!this.vols) return;
    this.totalPages = Math.ceil(this.vols.length / this.pageSize);
    const startIndex = (this.currentPage - 1) * this.pageSize;
    const endIndex = startIndex + this.pageSize;
    this.pagedVols = this.vols.slice(startIndex, endIndex);
  }

  nextPage() {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
      this.updatePagination();
    }
  }

  prevPage() {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.updatePagination();
    }
  }
}
