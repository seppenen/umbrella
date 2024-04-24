import {Children} from "react";


export const Each = <T,>({render, of}: { render: (item: T, index: number) => JSX.Element; of: T[];
}) => {
    return <>{Children.toArray(of.map(render))}</>;
};

