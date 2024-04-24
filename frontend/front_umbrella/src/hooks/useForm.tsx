import React, {ChangeEventHandler, useState} from "react";

interface FormValues {
    email: string;
    password: string;
}
export const useForm = (initialValues: FormValues): [FormValues, ChangeEventHandler<HTMLInputElement>] => {
    const [values, setValues] = useState<FormValues>(initialValues);

    const handleChange: ChangeEventHandler<HTMLInputElement> = (e) => {
        setValues(prevState => ({
            ...prevState,
            [e.currentTarget.name]: e.currentTarget.value
        }));
    }

    return [values, handleChange];
};
