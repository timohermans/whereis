import { useCallback, useEffect, useState } from "react";

declare global {
  interface WindowEventMap {
    storage_update: CustomEvent;
  }
}

type LocalStorageEventDetail = {
  key: string;
  value: string;
};

export function useLocalStorage<T>(
  key: string,
  initialValue: T | null,
): [T | null, (newValue: T | null) => void] {
  const eventName = "storage_update";
  const [value, setValue] = useState(initialValue);

  function updateValue(newValue: T | null) {
    if (value === newValue) return;
    window.dispatchEvent(
      new CustomEvent<LocalStorageEventDetail>(eventName, {
        detail: {
          key,
          value: JSON.stringify(newValue),
        },
      }),
    );
  }

  const readValue = useCallback(() => {
    const existingValue = localStorage.getItem(key);
    if (existingValue) {
      setValue(JSON.parse(existingValue));
    }
  }, [key]);

  const handleUpdate = useCallback(
    (e: CustomEvent<LocalStorageEventDetail>) => {
      if (e.detail.key !== key) return;
      const newValue = e.detail.value;

      localStorage.setItem(key, newValue);
      setValue(JSON.parse(newValue));
    },
    [key],
  );

  useEffect(() => {
    readValue();
    window.addEventListener(eventName, handleUpdate);

    return () => {
      window.removeEventListener(eventName, handleUpdate);
    };
  }, [handleUpdate, readValue]);

  return [value, updateValue];
}
