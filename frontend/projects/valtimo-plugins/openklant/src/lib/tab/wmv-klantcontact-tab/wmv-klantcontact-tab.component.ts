import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Document, DocumentService} from '@valtimo/document';
import {NgClass, NgForOf, NgIf} from '@angular/common';
import {Observable} from 'rxjs';
import {Klantcontact} from './model/klantcontact';
import {DocumentContent} from './model/document.content';
import {CarbonListModule, TimelineModule} from '@valtimo/components';

@Component({
    selector: 'app-wmv-klantcontact-tab',
    standalone: true,
    templateUrl: './wmv-klantcontact-tab.component.html',
    imports: [NgForOf, TimelineModule, NgClass, NgIf, CarbonListModule],
    styleUrl: './wmv-klantcontact-tab.component.scss'
})
export class WmvKlantcontactTabComponent implements OnInit {
    documentId: string;
    documentObserver: Observable<Document>;
    klantcontacts: Klantcontact[];

    constructor(
        private route: ActivatedRoute,
        private documentService: DocumentService,
    ) {
    }

    ngOnInit() {
        const snapshot = this.route.snapshot.paramMap;
        this.documentId = snapshot.get('documentId') || '';

        this.documentObserver = this.documentService.getDocument(this.documentId);
        this.documentObserver.subscribe(document => {
            const documentContent = document.content as DocumentContent;
            this.klantcontacts = documentContent.klantcontacten.reverse();
        });
    }

    formatDate(date: string): string {
        return new Date(date).toLocaleString('nl-NL', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric'
        });
    }

    formatTime(date: string): string {
        return new Date(date).toLocaleString('nl-NL', {
            hour: '2-digit',
            minute: '2-digit',
            hour12: false
        });
    }
}
