mardi 15 avril 2014 

nouvelles spécifications :
===============================================================================
class Abstract ObjetGeometrique :
===============================================================================
même chose que la V1. On a retirer le champ size.

#ICI C'est nouveau 

===============================================================================
class  : Comportement objet
===============================================================================
C'est presque l'équivalent de la l'ancienne spécification de la class Animation.

Un comportement c'est l'union d'un objet est de son animation. Il n'y à qu'un par objet.
Cette objet sera appelé par la classe GestionAnimation.

champs:
-------
-ObjetGeometrique objgeo :
	c'est l'objet géométrique
-Animation anim :
	contient toutes les animation associer à l'objet.
	
méthodes:
---------
pas encore d'idées

===============================================================================
class Animation :
===============================================================================
Une animation pourra être composée de plusieurs annimation.
Elle sera basée sur le design pattern Composite.
Une feuille sera une animation simple (translation,couleur...)
Une animation composé sera composé de plusieurs animations simple.
(voir si stop au premier niveau pour les animations enfants.


champs:
-------
-int t_start :
	temps de départ de l'animation.
	(réfléchir : Une animation fille pointe sur le t_start parent)
	
-int t_départ :
	temps de fin de l'animation
	
-Easing e :
	objet easing function
	(voir plus bas) #TODO : à rédiger
	
-listAnimation la:
	contient les animations filles.
	Voir quel type de conteneur (probablement clé, valeur)

methodes:
---------
+AffineTransformation getTransformation(temps_courrant)
	Fait appel aux méthodes de transformations de l'objet géométrique.
	Retourne la synthèse de toutes les transformations dans un objet AffineTransformation. 
	(utiliser méthode"concatenate")
	Retourne null si aucune tranformations.

+boolean add(Animation a) :
	Pour ajouter une nouvelle animation.
	Sauf que cette méthode va vérifier si l'animation n'a pas
	d'intersection temporel avec une autre.
	retourne faux si pas possible et/ou une exception pour savoir l'erreur.

#proposition :
+boolean addToAnim(Animation cible,Animation a):
	Cette méthode ajoute une animation dans une animation déja présente.
	
#proposition :
+boolean isRootParent():
	retourne vrai si nous somme un parent racine (this = pas de parent)
	Utile pour t_start et t_stop. Une Annimation fille fait référence 
	à son parent pour t_start et t_stop.
	

	
#pour le reste c'est les méthodes du paterne Composite...
#TODO : spécifier comment marche une animation simple
===============================================================================
class Easing :
===============================================================================
c'est la classe qui sera untilisé pour faire des fonction easing
#TODO : pas fini

