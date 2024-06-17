import {Container} from 'inversify';

interface IMain {
    container: Container;
    app: App;
}

async function main(): Promise<IMain> {
    const container = new Container();
    container.load(bindings);
    const app = container.get<App>(TYPES.Application);
    await app.init();
    return { container, app };
}

export const boot = main();
