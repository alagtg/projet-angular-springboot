import { bootstrapApplication } from '@angular/platform-browser';
import { provideHttpClient } from '@angular/common/http';
import { AppComponent } from './app/app.component';
import { config } from './app/app.config.server';  // ⚡ config spécifique server

const bootstrap = () =>
  bootstrapApplication(AppComponent, {
    providers: [
      provideHttpClient(),
      ...config.providers || []   // ⚡ garde aussi les providers server
    ]
  });

export default bootstrap;
