def cifrar_atbash(mensaje):
    alfabeto_minuscula = "abcdefghijklmnopqrstuvwxyz"
    alfabeto_mayuscula = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    
    invertido_minus = alfabeto_minuscula[::-1]
    invertido_mayus = alfabeto_mayuscula[::-1]
    
    diccionario_crifrado = {}
    for i in range(26):
        diccionario_crifrado[alfabeto_minuscula[i]] = invertido_minus[i]
        diccionario_crifrado[alfabeto_mayuscula[i]] = invertido_mayus[i]
    
    mensaje_cifrado = ""
    for letra in mensaje:
        mensaje_cifrado += diccionario_crifrado.get(letra, letra)
    
    return mensaje_cifrado

def descifrar_atbash(mensaje_cifrado):
    return cifrar_atbash(mensaje_cifrado)

mensaje=input("Mensaje: ")
texto_cifrado = cifrar_atbash(mensaje)
print("Mensaje cifrado:", texto_cifrado)
texto_descifrado = descifrar_atbash(texto_cifrado)
print("Mensaje descifrado:", texto_descifrado)


