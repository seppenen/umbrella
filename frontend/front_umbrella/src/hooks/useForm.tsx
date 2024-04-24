import React, {ChangeEventHandler, useState} from "react";

interface FormValues {
    email: string;
    password: string;
}
export const useForm = (initialValues: FormValues) => {
    const [values, setValues] = useState(initialValues);

    const handleChange: ChangeEventHandler<HTMLInputElement> = (e) => {
        setValues(prevState => ({
            ...prevState,
            [e.currentTarget.name]: e.currentTarget.value
        }));
    }

    return { values, handleChange };
};
