lundi 14 avril 2014, 19:04:42 (UTC+0200)

synthèse de ce qui à été dis  :

#Spécifiactions des classes version en cours modélisation :

===============================================================================
class Abstract ObjetGeometrique :
===============================================================================
correspond au dessin de l'objet.
Les classes filles seront cercle, carré, rond, triangle....

champs :
--------	
-String nom :
	nom de l'objet

-Point centre :
	position centrale de l'objet

-Size size:
	taille de l'objet
	
-Color fill_color:
	couleur de fond de l'objet

-Color stroke_color:
	couleur de trait de contours

-Shape forme: 
	forme de l'objet (j'ai mis shape mais ça sera probablement un Rectangle2D)
	javadoc :   http://docs.oracle.com/javase/7/docs/api/java/awt/geom/Rectangle2D.html
	exemple :  http://www.roseindia.net/java/example/java/awt/how-to-create-circle-in-java.shtml
	
	cas d'utilisation ; faire un cercle :
	Shape circle = new Ellipse2D.Float(100.0f, 100.0f, 100.0f, 100.0f)
	
-AffineTransforme trans :
	contient toutes les tranformations appliqué à l'objet
	javadoc : http://docs.oracle.com/javase/7/docs/api/java/awt/geom/AffineTransform.html
	exemple : http://www.javalobby.org/java/forums/t19387.html
#COMMENT : je met l'objet AffineTransforme ici mais je pense qu'il faut réfléchir 
#à ce qu'est une transformation "Est ce que une tranformation appartient à l'objet lui même?"
#dans le sujet il semble que la transformation appartienne à l'animation 

méthodes :
----------
+Point getCoord() :
	pour récupérer les coordonées de l'angle haut gauche(pour le dessin)

+void DessineGraphics() :
	pour dessiner le graphique dans l'objet dessin

+Getteur/Setteur qui vont bien
	
#possède les méthodes de tranformations :
elles aurons un nom qui commence par transXXX
Avec les paramètres suivant temps_depart, temps_fin,
temps_courant. Elle applique la transformation au champ "trans"
Pas de retour. Lance une exception si la transformation est impossible.



	temps_depart/temps_fins : pour connaitre l'intervale de temps

	temps_courrant : pour savoir à quel moment de l'intervale de temps on est
		(exception si en dehors de l'intervalle)	


##proposition pour translation :

+void transTranslation(liste_de_points , easing_function = 0, temps_depart ,
temps_fin, temps_courrant):

	liste_de_points : la liste des points par lesquels passe notre objet 
	durant l'animation (utiliser GeneralPath).

	easing_function : coorespond a la fonction easing qui sera appliqué 
	(faire un enum 0 : lineaire, 1 = courbe_exp, 2= courbe_log ...)



##proposition pour rotation 2 types:

+void transRotationCentrale(sens, easing_function = 0,temps_depart ,temps_fin, temps_courrant) :
	faire une rotaion par rapport à son propre centre de rotation
	
	sens : sens de rotation
	
+void transRotationExt(centre ,sens,temps_depart ,temps_fin, temps_courrant) :
	prend un centre de rotation exterrieur pour faire la rotation
	centre : c'est le centre de rotation exterieur avec lequel on va tourner
	
#proposition pour transformation de l'épaisseur du trait :

+void transStroke(BasicStroke final_stroke , easing_function = 0 ,temps_depart ,temps_fin, temps_courrant) :
	final_stroke : c'est lépaisseur final que doit prendre le stroke

#prosition transformation couleur (possibilité de la faire pour le trait):

+void transCouleurFond(couleur_final, easing_function = 0 ,temps_depart ,temps_fin, temps_courrant)
	couleur_final : couleur que doit prendre l'objet à la fin de la transformation


##QUESTION : Voir si il faut faire une fonction qui réunis toutes les transformations et les fait en même temps?

===============================================================================
class Animation :
===============================================================================
une annimation concerne un objet elle contient toutes les tranformations à 
appliquer dans le temps. Et controle si les tranformations sont cohérente.
Exemple : si deux tranformations de trype translations s'entre coupent dans le temps c'est rejeté par 

champs :
--------
-String nom:
	nom de l'animation
	(peut être calculer à partir du nom de l'objetGeometrique)
	
-ObjetGeometrique objGeo:
	l'objet géométrique concerné par l'animation
	
-ListeTransformations :
	contient toutes les tranformations leurs temps de départ et leurs temps de fin
#PROBLEME : "Une animation est l’application temporalisée d’une transformation géométrique"
#COMMENT : il faut spécifier comment on va procéder pour faire la liste des transformations



méthodes :
---------
+AffineTransformation getTransformation(temps_courrant)
	Fait appel aux méthodes de transformations de l'objet géométrique.
	Retourne la synthèse de toutes les transformations dans un objet AffineTransformation. 
	(utiliser méthode"concatenate")
	Retourne null si aucune tranformations.
	
+void AjouteTransformation(...):
	ajoute des transformations à la liste des transformations.
	possibilité :
	------------
	lance une exception si la transformation est en collision avec une autre.

===============================================================================
class Gestionaire d'animation
===============================================================================
Continent la liste des animations et dessine les objets présents dans le panel.
C'est lui qui fait tourner l'horloge de l'animation.

champs:
-------
-ListeAnimations lA :
	liste des animations à faire (avec leurs objets associer)
	
TODO : à finir .....

