# condingame
Repo pour les challenges codingame

## Principes généraux du bot

* On ne cible surtout pas les monstres qui se dirigent vers la base adverse
* On cible en préférence les monstres qui se dirigent vers notre base
* On cible en préférence les monstres les plus prés de la base

## Formule d'interception

Soit un monstre ayant les coordonnées xm,ym et les vitesses de déplacement vx,vy.

A l'instant t, il sera aux positions x(t),y(t) avec
- x(t) = xm + vxm * t
- y(t) = ym + vym * t

Pour que le héros le rejoigne à l'instant t, il faut que 
- x(t) = xh + vxh * t
- y(t) = yh + vyh * t
- sqrt(vxh² + vyh²) = 800

On a donc le système suivant :
- vxh² + vyh² = 640000
- xm + vxm * t = xh + vxh * t
- ym + vym * t = yh + vyh * t


- vxh = (xm - xh + vxm * t)/t
- vyh = (ym - yh + vym * t)/t


- vxh = (xm - xh)/t + vxm
- vyh = (ym - yh)/t + vym

Bref, c'est beaucoup trop compliqué!!!!
Et on fait pas des maths, on s'en fout d'être parfaitement exact, donc on va essayer de trouver une autre formule plus simple, même si on a pas prouvé qu'elle était juste

## Nouvelle formule d'interception

On va d'abord calculer la pente de la droite passant par le monstre et le héros:
* p1 = (ym - yh)/(xm - xh)

On va ensuite calculer la pente de ses perpendiculaires:
* p2 = -1/p1 = -1/((ym - yh)/(xm - xh)) = -(xm - xh)/(ym - yh)

On va maintenant calculer le point entre point entre le monstre et le héros qui est deux fois plus proche du monstre que du héros
* xp = 2 * xm - xh
* yp = 2 * ym - yh

Il ne nous reste plus qu'à calculer le point d'intersection de la droite d1 de déplacement du monstre et de la droite d2 passant par xp,yp avec le coefficient directeur p2

Pour d1 :
* y = (vym/vxm) * x + b
* ym = (vym/vxm) * xm + b
* b = ym - (vym/vxm) * xm

On obtient donc l'équatoin de d1 : y = (vym/vxm) * x + ym - (vym/vxm) * xm

Pour d2 :
* y = -(xm - xh)/(ym - yh) * x + b
* 2 * ym - yh = -(xm - xh)/(ym - yh) * (2 * xm - xh) + b
* b = 2 * ym - yh + (xm - xh)/(ym - yh) * (2 * xm - xh)

On obtient donc l'équation de d2 : y = -(xm - xh)/(ym - yh) * x + 2 * ym - yh + (xm - xh)/(ym - yh) * (2 * xm - xh)

Mais du coup je me rends compte que cette idée semble marcher si le héros est devant le monstre, mais que ça marche pas du tout si derrière.

Je vais donc m'arrêter ici pour le moment.
