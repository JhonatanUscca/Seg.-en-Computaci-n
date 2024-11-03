
def cifrar_polybios(mensaje):
  alfabeto = 'ABCDEFGHIKLMNOPQRSTUVWXYZ'

  matriz = {alfabeto[i]: (i // 5 + 1, i % 5 + 1) for i in range(len(alfabeto))}

  mensaje_cifrado = ''
  for letra in mensaje.upper():
    if letra in matriz:
      fila, columna = matriz[letra]
      mensaje_cifrado += str(fila) + str(columna)
    else:
      mensaje_cifrado += letra
  return mensaje_cifrado


def romper_cifrado(mensaje_cifrado):
  alfabeto = 'ABCDEFGHIKLMNOPQRSTUVWXYZ'

  mensaje = ''
  for i in range(len(mensaje_cifrado) //2):
    fila = mensaje_cifrado[i*2]
    columna = mensaje_cifrado[((i+1)*2)-1]
    indice = (int(fila) - 1) * 5 + (int(columna) - 1)
    letra = alfabeto[indice]
    mensaje += letra
  return mensaje

mensaje_entrada = "CRIPTO"
print ("Entrada: ", mensaje_entrada)
print ("Salidad cifrada: ", cifrar_polybios(mensaje_entrada))


print ("\nIntentado romper el cifrado")
mensaje_cifrado = cifrar_polybios(mensaje_entrada)
print ("Entrada: ", mensaje_cifrado)
print ("Salidad cifrada: ", romper_cifrado(mensaje_cifrado))