import { Routes } from '@angular/router';
import { authGuard } from './auth.guard';
import { ProjectList } from './project-list-component/project-list';
import { Login } from './login/login';
import { ProfileComponent } from './profile-component/profile-component';
import { ProjectFormComponent } from './project-form-component/project-form-component';
import { ProjectDetailComponent } from './project-detail-component/project-detail-component';
import { AddMemberFormComponent } from './add-member-form-component/add-member-form-component';

export const routes: Routes = [
    { path: 'projects', children: [
        { path: '', component: ProjectList}, 
        { path: 'new', component: ProjectFormComponent},
        { path: ':id/members', component: AddMemberFormComponent},
        {path: ':id', component: ProjectDetailComponent}],
        canActivate:[authGuard]
    },
    { path: 'login', component: Login },
    { path: 'profile', component: ProfileComponent, canActivate: [authGuard] },
    { path: '', redirectTo: '/projects', pathMatch: 'full' },
    { path: '**', redirectTo: '/projects', pathMatch: 'full' }
];  