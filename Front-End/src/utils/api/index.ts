import { UserAccount, User, Transaction, Card } from '../../types';

const myInit = (method = 'GET', token?: string) => {
  const headers: HeadersInit = {
    'Content-Type': 'application/json',
  };
  // Solo agregar la cabecera Authorization si hay un token
  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }
  return {
    method,
    headers,
    mode: 'cors' as RequestMode,
    cache: 'default' as RequestCache,
  };
};

const myRequest = (endpoint: string, method: string, token?: string) =>
  new Request(endpoint, myInit(method, token));

const baseUrl = 'http://localhost:8085';

const rejectPromise = (response?: Response): Promise<Response> =>
  Promise.reject({
    status: (response && response.status) || '00',
    statusText: (response && response.statusText) || 'Ocurrió un error',
    err: true,
  });

export const login = (email: string, password: string) => {
  return fetch(myRequest(`${baseUrl}/users/login`, 'POST'), {
    body: JSON.stringify({ email, password }),
    credentials: "include"
  })
    .then((response) => {
      if (response.ok) {
        return response.json();
      }
      return rejectPromise(response);
    })
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};

export const createAnUser = (user: User) => {
  return fetch(myRequest(`${baseUrl}/users/register`, 'POST'), {
    method: 'POST', // Asegúrate de especificar el método
    headers: {
      'Content-Type': 'application/json', // Indica que el cuerpo es JSON
    },
    body: JSON.stringify(user),
    credentials: "include"
  })
    .then((response) => {
      if (response.ok) {
        return response.json().then(data => {
          // Procesar la respuesta aquí si es necesario
          return data; // Puedes retornar la respuesta completa
        });
      }
      return rejectPromise(response);
    })
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};

export const confirmUserRegistration = (confirmationCode: string, email: string) => {
  return fetch(`${baseUrl}/users/verify`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ verificationCode: confirmationCode, email }), // Incluir el email en el objeto
  })
  .then(async (response) => {
    const text = await response.text(); // Lee el cuerpo como texto una sola vez

    if (!response.ok) {
      throw new Error(text || 'Error en la confirmación del registro');
    }

    try {
      return JSON.parse(text); // Si es JSON, intenta parsear
    } catch (e) {
      return text; // Si no es JSON, devuelve el texto directamente (p.ej., mensaje de éxito)
    }
  });
};


export const logout = (token: string) => {
  return fetch(myRequest(`${baseUrl}/users/logout`, 'POST', token))
    .then((response) => {
      if (response.ok) {
        return response.json();
      }
      return rejectPromise(response);
    })
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};

export const getUser = (id: number): Promise<User> => {
  return fetch(myRequest(`${baseUrl}/users/${id}`, 'GET'))
    .then((response) =>
      response.ok ? response.json() : rejectPromise(response)
    )
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};

export const updateUser = (
  id: string,
  data: any,
  token: string
): Promise<Response> => {
  return fetch(myRequest(`${baseUrl}/users/${id}`, 'PATCH', token), {
    body: JSON.stringify(data),
  })
    .then((response) =>
      response.ok ? response.json() : rejectPromise(response)
    )
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};

export const getAccount = (id: number, token: string): Promise<UserAccount> => {
  return fetch(myRequest(`${baseUrl}/accounts/${id}`, 'GET', token), {})
    .then((response) => {
      if (response.ok) {
        // Devuelve directamente el objeto de cuenta sin usar el índice [0]
        return response.json();
      }
      return rejectPromise(response);
    })
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};


export const getAccounts = (token: string): Promise<UserAccount[]> => {
  return fetch(`${baseUrl}/accounts`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
  })
    .then((response) => {
      if (!response.ok) {
        console.error('Error en la respuesta:', response); // Loguea la respuesta para obtener más detalles
        throw new Error(`HTTP error! status: ${response.status}`); // Lanza un error si la respuesta no es 200
      }
      return response.json();
    })
    .catch((err) => {
      console.log('Error:', err);
      return rejectPromise(err);
    });
};


export const updateAccount = (
  id: number,
  data: any,
  token: string
): Promise<Response> => {
  return fetch(myRequest(`${baseUrl}/accounts/update/alias/${id}`, 'PATCH', token), {
    body: JSON.stringify(data),
  })
    .then((response) =>
      response.ok ? response.json() : rejectPromise(response)
    )
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};

export const getUserActivities = async (
  id: number,
  token: string
): Promise<Transaction[]> => {
  try {
    const response = await fetch(
      myRequest(`${baseUrl}/accounts/${id}/activity`, 'GET', token)
    );

    if (!response.ok) {
      // Rechaza la promesa si la respuesta no es OK
      return Promise.reject(await response.json());
    }

    // Si la respuesta es correcta, devuelve los datos parseados
    const data: Transaction[] = await response.json();
    return data;
  } catch (error) {
    console.error('Error fetching user activities:', error);
    // Rechaza la promesa en caso de un error de red o fallo de la API
    return Promise.reject(error);
  }
};


export const getUserActivity = (
  userId: number,
  activityId: number,
  token: string
): Promise<Transaction> => {
  return fetch(
    myRequest(
      `${baseUrl}/accounts/${userId}/activity/${activityId}`,
      'GET',
      token
    )
  )
    .then((response) => {
      if (response.ok) {
        return response.json();
      }
      return rejectPromise(response);
    })
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};

export const getUserCards = (
  userId: number,
  token: string
): Promise<Card[]> => {
  return fetch(myRequest(`${baseUrl}/accounts/${userId}/cards`, 'GET', token))
    .then((response) => {
      if (response.ok) {
        return response.json();
      }
      return rejectPromise(response);
    })
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};

export const getUserCard = (userId: string, cardId: string): Promise<Card> => {
  return fetch(myRequest(`${baseUrl}/accounts/${userId}/cards/${cardId}`, 'GET'))
    .then((response) => {
      if (response.ok) {
        return response.json();
      }
      return rejectPromise(response);
    })
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};

export const deleteUserCard = (
  userId: number,
  cardId: string,
  token: string
): Promise<Response> => {
  return fetch(
    myRequest(`${baseUrl}/accounts/${userId}/cards/${cardId}`, 'DELETE', token)
  )
    .then((response) => {
      if (response.ok) {
        return response.json();
      }
      return rejectPromise(response);
    })
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};

export const createUserCard = (
  userId: number,
  card: any,
  token: string
): Promise<Response> => {
  return fetch(myRequest(`${baseUrl}/accounts/${userId}/cards`, 'POST', token), {
    body: JSON.stringify(card),
  })
    .then((response) =>
      response.ok ? response.json() : rejectPromise(response)
    )
    .catch((err) => {
      console.log(err);
      return rejectPromise(err);
    });
};

// TODO: edit when backend is ready
export const createDepositActivity = (
  userId: number,
  amount: number,
  cardId: number,
  token: string
) => {
  const maxAmount = 30000;
  if (amount > maxAmount) return rejectPromise();

  const activity = {
    cardId,
    amount
  };

  return fetch(
    myRequest(`${baseUrl}/accounts/${userId}/transferences/cards`, 'POST', token),
    {
      body: JSON.stringify(activity),
    }
  )
  .then((response) => {
    // Primero leemos el cuerpo como texto
    return response.text().then((text) => {
      // Ahora intentamos convertir el texto a JSON
      try {
        const jsonData = JSON.parse(text);
        return jsonData; // Retorna el objeto JSON si se parsea correctamente
      } catch {
        return text; // Retorna el texto plano si no se puede parsear
      }
    });
  })
  .then((data) => {
    // Aquí puedes manejar tanto objetos JSON como texto
    console.log(data); // Manejar la respuesta, sea texto o JSON
    return data; // Retorna la respuesta final
  })
  .catch((err) => {
    console.log(err);
    return rejectPromise(err);
  });
};


// TODO: edit when backend is ready
export const createTransferActivity = (
  userId: number,
  token: string,
  origin: string,
  destination: string,
  amount: number,
  name?: string
) => {
  return fetch(
    myRequest(`${baseUrl}/accounts/${userId}/transferences/money`, 'POST', token),
    {
      method: 'POST', // Asegúrate de incluir el método POST
      headers: {
        'Content-Type': 'application/json', // Especifica el tipo de contenido
        Authorization: `Bearer ${token}`, // Si es necesario, agrega el token aquí
      },
      body: JSON.stringify({
        amount,
        recipient: destination,
      }),
    }
  )
  .then((response) => {
    if (!response.ok) {
      // Si la respuesta no es 2xx, leer el texto de la respuesta
      return response.text().then((text) => {
        const error = {
          message: `Error en la transferencia: ${response.status}`,
          status: response.status,
          body: text, // Guardar el texto de error si es necesario
        };
        throw error; // Lanzar el error
      });
    }
    
    // Primero leemos el cuerpo como texto
    return response.text().then((text) => {
      // Ahora intentamos convertir el texto a JSON
      try {
        const jsonData = JSON.parse(text);
        return jsonData; // Retorna el objeto JSON si se parsea correctamente
      } catch {
        return text; // Retorna el texto plano si no se puede parsear
      }
    });
  })
  .then((data) => {
    // Aquí puedes manejar tanto objetos JSON como texto
    console.log(data); // Manejar la respuesta, sea texto o JSON
    return data; // Retorna la respuesta final
  })
  .catch((err) => {
    console.log(err);
    return Promise.reject(err); // Cambiar a Promise.reject para que se propague el error
  });
};



export const downloadReceipt = async (userId: number, token: string, activityId: number): Promise<void> => {
  const url = `${baseUrl}/accounts/${userId}/activity/${activityId}/receipt`; // Asegúrate de que esta URL sea correcta

  try {
    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`, // Incluye el token en la cabecera
        'Content-Type': 'application/pdf', // Asegúrate de que el servidor maneje esto
      },
    });

    if (!response.ok) {
      throw new Error('Error al descargar el recibo');
    }

    // Crea un blob a partir de la respuesta
    const blob = await response.blob();

    // Crea un enlace para descargar el archivo
    const link = document.createElement('a');
    link.href = window.URL.createObjectURL(blob); // Asigna el blob a la URL
    link.download = `receipt_${activityId}.pdf`; // Nombre del archivo a descargar

    // Añadir el enlace al DOM
    document.body.appendChild(link);
    link.click(); // Simular clic en el enlace
    document.body.removeChild(link); // Eliminar el enlace del DOM
  } catch (error) {
    console.error('Error al descargar el recibo:', error);
  }
};

export const sendPasswordResetEmail = (email: string) => {
  const formData = new URLSearchParams();
  formData.append('email', email); // Añade el email como parámetro

  return fetch(`${baseUrl}/users/request-password-reset`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded', // Cambia a x-www-form-urlencoded
    },
    body: formData.toString(), // Envía los datos como una cadena de parámetros
  })
  .then(async (response) => {
    const text = await response.text();

    if (!response.ok) {
      throw new Error(text || 'Error al enviar el correo de recuperación de contraseña');
    }

    try {
      return JSON.parse(text);
    } catch (e) {
      return text;
    }
  });
};


export const resetPassword = (token: string, newPassword: string, confirmPassword: string) => {
  const formData = new URLSearchParams();
  formData.append('token', token); // Añade el token como parámetro
  formData.append('newPassword', newPassword); // Añade la nueva contraseña como parámetro
  formData.append('confirmPassword', confirmPassword); // Añade la confirmación de la nueva contraseña como parámetro

  return fetch(`${baseUrl}/users/reset-password`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded', // Cambia a x-www-form-urlencoded
    },
    body: formData.toString(), // Envía los datos como una cadena de parámetros
  })
  .then(async (response) => {
    const text = await response.text(); // Lee el cuerpo como texto una sola vez

    if (!response.ok) {
      throw new Error(text || 'Error al restablecer la contraseña');
    }

    try {
      return JSON.parse(text); // Si es JSON, intenta parsear
    } catch (e) {
      return text; // Si no es JSON, devuelve el texto directamente (por ejemplo, mensaje de éxito)
    }
  })
  .catch((error) => {
    throw new Error(error.message || 'Error en la solicitud de restablecimiento de contraseña');
  });
};












