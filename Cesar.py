def cifrar_cesar(texto, desplazamiento):
    resultado = ""
    for char in texto:
        if char.isalpha():
            # Determinar el código ASCII base (65 para mayúsculas, 97 para minúsculas)
            ascii_base = 65 if char.isupper() else 97
            # Convertir a número (0-25), aplicar desplazamiento y volver a convertir a letra
            nuevo_char = chr((ord(char) - ascii_base + desplazamiento) % 26 + ascii_base)
            resultado += nuevo_char
        else:
            # Mantener caracteres no alfabéticos sin cambios
            resultado += char
    return resultado
def descifrar_cesar(texto_cifrado, desplazamiento):
    # Para descifrar, usamos el desplazamiento negativo
    return cifrar_cesar(texto_cifrado, -desplazamiento)
def romper_cifrado(texto_cifrado):
    # Intenta romper el cifrado mostrando todas las posibles combinaciones.
    posibles_textos = []
    for i in range(26):
        texto_descifrado = descifrar_cesar(texto_cifrado, i)
        posibles_textos.append(f"Desplazamiento {i}: {texto_descifrado}")
    return posibles_textos

if __name__ == "__main__":
    # Ejemplo de cifrado
    texto_original = "Hola Mundo!"
    desplazamiento = 3
    texto_cifrado = cifrar_cesar(texto_original, desplazamiento)
    texto_descifrado = descifrar_cesar(texto_cifrado, desplazamiento)
    print(f"Texto original: {texto_original}")
    print(f"Texto cifrado: {texto_cifrado}")
    print(f"Texto descifrado: {texto_descifrado}")
    # Ejemplo de "romper" el cifrado
    print("\nIntentando romper el cifrado:")
    posibles_soluciones = romper_cifrado(texto_cifrado)
    for solucion in posibles_soluciones:
        print(solucion)