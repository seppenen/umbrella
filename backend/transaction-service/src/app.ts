import express, {Express} from 'express';
import {injectable} from 'inversify';
import {Server} from 'http';

@injectable()
export class App {
    app:Express
    server:Server
    port = process.env.PORT

    constructor() {
        this.app = express();
    }

}

